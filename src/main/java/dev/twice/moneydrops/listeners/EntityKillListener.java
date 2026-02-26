package dev.twice.moneydrops.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import dev.twice.moneydrops.services.RewardService;

import org.jetbrains.annotations.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntityKillListener implements Listener {

    private final RewardService rewardService;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof final Player killer) {
            rewardService.processKill(killer, event.getEntity());
        }
    }
}