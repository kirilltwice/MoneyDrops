package dev.twice.moneydrops.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public interface RewardModifier {
    double modify(final @NotNull Player player, final @NotNull Entity victim, final double currentReward);
}
