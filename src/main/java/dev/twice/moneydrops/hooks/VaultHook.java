package dev.twice.moneydrops.hooks;

import dev.twice.moneydrops.DropsPlugin;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VaultHook {

    private final DropsPlugin plugin;
    @Getter
    private Economy economy;

    public boolean setup() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public void deposit(final Player player, final double amount) {
        if (economy != null) {
            economy.depositPlayer(player, amount);
        }
    }

    public void withdraw(final Player player, final double amount) {
        if (economy != null) {
            economy.withdrawPlayer(player, amount);
        }
    }

    public double getBalance(final Player player) {
        return economy != null ? economy.getBalance(player) : 0.0;
    }
}

