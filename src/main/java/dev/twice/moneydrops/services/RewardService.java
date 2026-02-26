package dev.twice.moneydrops.services;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import dev.twice.moneydrops.DropsPlugin;
import dev.twice.moneydrops.api.MoneyDropsAPI;
import dev.twice.moneydrops.api.RewardModifier;
import dev.twice.moneydrops.api.events.MoneyDropEvent;
import dev.twice.moneydrops.config.ConfigManager;
import dev.twice.moneydrops.hooks.VaultHook;
import dev.twice.moneydrops.utility.ColorUtility;

import org.jetbrains.annotations.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RewardService {

    private final DropsPlugin plugin;
    private final ConfigManager config;
    private final VaultHook vault;

    public void processKill(final @NotNull Player killer, final @NotNull Entity victim) {
        final CreatureSpawnEvent.SpawnReason reason = victim.getEntitySpawnReason();
        if (MoneyDropsAPI.getInstance().isSpawnReasonIgnored(reason)) {
            return;
        }

        final double baseReward = calculateBaseReward(killer, victim);

        if (baseReward <= 0) return;

        double finalReward = baseReward;

        for (final RewardModifier modifier : MoneyDropsAPI.getInstance().getModifiers()) {
            finalReward = modifier.modify(killer, victim, finalReward);
        }

        finalReward *= MoneyDropsAPI.getInstance().getPlayerMultiplier(killer);

        final MoneyDropEvent event = new MoneyDropEvent(killer, victim, finalReward);
        plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getFinalReward() <= 0) return;

        giveReward(killer, event.getFinalReward());
    }

    public double calculateBaseReward(final @NotNull Player killer, final @NotNull Entity victim) {
        double baseReward = switch (victim) {
            case final Player target -> {
                final double victimBalance = vault.getBalance(target);
                yield (victimBalance * config.getPlayersPercentage()) / 100.0;
            }
            case final Monster ignored -> randomBetween(config.getMonstersMin(), config.getMonstersMax());
            default -> randomBetween(config.getAmbientMin(), config.getAmbientMax());
        };

        if (killer.hasPermission("moneydrops.premium")) {
            baseReward *= config.getPremiumMultiplier();
        }

        return baseReward;
    }

    public void giveReward(final @NotNull Player killer, final double reward) {
        vault.deposit(killer, reward);
        killer.sendActionBar(ColorUtility.colorize(String.format(config.getKillMessage(), String.format("%.2f", reward))));
    }

    private double randomBetween(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}