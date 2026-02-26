package dev.twice.moneydrops.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MoneyDropEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player killer;
    private final Entity victim;
    private final double baseReward;

    @Setter
    private double finalReward;
    private boolean cancelled;

    public MoneyDropEvent(final @NotNull Player killer, final @NotNull Entity victim, final double baseReward) {
        this.killer = killer;
        this.victim = victim;
        this.baseReward = baseReward;
        this.finalReward = baseReward;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
