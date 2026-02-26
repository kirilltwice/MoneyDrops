package dev.twice.moneydrops;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import dev.twice.moneydrops.api.MoneyDropsAPI;
import dev.twice.moneydrops.config.ConfigManager;
import dev.twice.moneydrops.hooks.VaultHook;
import dev.twice.moneydrops.listeners.EntityKillListener;
import dev.twice.moneydrops.services.ListenerService;
import dev.twice.moneydrops.services.RewardService;
import lombok.Getter;

@Getter
public final class DropsPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        vaultHook = new VaultHook(this);
        if (!vaultHook.setup()) {
            getLogger().severe("Vault not found! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        final RewardService rewardService = new RewardService(this, configManager, vaultHook);

        new MoneyDropsAPI(rewardService);

        new ListenerService(this, Set.of(
                new EntityKillListener(rewardService)
        )).register();
    }
}