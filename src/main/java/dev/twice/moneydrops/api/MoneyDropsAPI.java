package dev.twice.moneydrops.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import dev.twice.moneydrops.services.RewardService;

import org.jetbrains.annotations.NotNull;
import lombok.Getter;

public class MoneyDropsAPI {

    @Getter
    private static MoneyDropsAPI instance;
    private final RewardService rewardService;

    private final List<RewardModifier> modifiers = new ArrayList<>();
    private final Set<CreatureSpawnEvent.SpawnReason> ignoredReasons = new HashSet<>();
    private final Map<UUID, TemporaryMultiplier> multipliers = new ConcurrentHashMap<>();

    public MoneyDropsAPI(@NotNull RewardService rewardService) {
        this.rewardService = rewardService;
        instance = this;
    }

    public double calculateReward(final @NotNull Player killer, final @NotNull Entity victim) {
        return rewardService.calculateBaseReward(killer, victim);
    }

    public void giveReward(final @NotNull Player player, final double amount) {
        rewardService.giveReward(player, amount);
    }

    public void addTemporaryMultiplier(final @NotNull Player player, final double multiplier, final long durationMillis) {
        multipliers.put(player.getUniqueId(), new TemporaryMultiplier(multiplier, System.currentTimeMillis() + durationMillis));
    }

    public double getPlayerMultiplier(final @NotNull Player player) {
        final TemporaryMultiplier tm = multipliers.get(player.getUniqueId());
        if (tm != null) {
            if (System.currentTimeMillis() > tm.expirationTime()) {
                multipliers.remove(player.getUniqueId());
                return 1.0;
            }
            return tm.multiplier();
        }
        return 1.0;
    }

    public void registerModifier(final @NotNull RewardModifier modifier) {
        modifiers.add(modifier);
    }

    public void unregisterModifier(final @NotNull RewardModifier modifier) {
        modifiers.remove(modifier);
    }

    @NotNull
    public List<RewardModifier> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    public void addIgnoredSpawnReason(final @NotNull CreatureSpawnEvent.SpawnReason reason) {
        ignoredReasons.add(reason);
    }

    public void removeIgnoredSpawnReason(final @NotNull CreatureSpawnEvent.SpawnReason reason) {
        ignoredReasons.remove(reason);
    }

    public boolean isSpawnReasonIgnored(final @NotNull CreatureSpawnEvent.SpawnReason reason) {
        return ignoredReasons.contains(reason);
    }

    private record TemporaryMultiplier(double multiplier, long expirationTime) {}
}
