package dev.twice.moneydrops.services;

import java.util.Collection;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerService {

    private final Plugin plugin;
    private final Set<Listener> listeners;

    public void register() {
        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    public void register(final Listener... listeners) {
        for (final Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void register(final Collection<Listener> listeners) {
        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}