package dev.twice.moneydrops.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import dev.twice.moneydrops.DropsPlugin;
import lombok.Getter;

@Getter
public class ConfigManager {

    private final DropsPlugin plugin;
    private final FileConfiguration config;

    private double ambientMin;
    private double ambientMax;
    private double monstersMin;
    private double monstersMax;
    private double playersPercentage;
    private double premiumMultiplier;
    private String killMessage;

    public ConfigManager(final @NotNull DropsPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        loadConfig();
    }

    private void loadConfig() {
        final String ambient = config.getString("settings.ambient");
        if (ambient != null) {
            final int ambientSplit = ambient.indexOf('~');
            if (ambientSplit != -1) {
                this.ambientMin = Double.parseDouble(ambient.substring(0, ambientSplit));
                this.ambientMax = Double.parseDouble(ambient.substring(ambientSplit + 1));
            }
        }

        final String monsters = config.getString("settings.monsters");
        if (monsters != null) {
            final int monstersSplit = monsters.indexOf('~');
            if (monstersSplit != -1) {
                this.monstersMin = Double.parseDouble(monsters.substring(0, monstersSplit));
                this.monstersMax = Double.parseDouble(monsters.substring(monstersSplit + 1));
            }
        }

        this.playersPercentage = config.getDouble("settings.players-percentage");
        this.premiumMultiplier = config.getDouble("settings.premium-multiplier", 1.5);
        this.killMessage = config.getString("message.kill");
    }
}