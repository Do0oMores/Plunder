package top.mores.plunder.Utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Random;

public class VaultUtil {

    ConfigUtil config = new ConfigUtil();
    Random rand = new Random();
    static Economy economy;

    private double randomAmount(double min, double max) {
        return rand.nextDouble((max - min) + 1) + min;
    }

    //初始化经济
    public static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    //增加玩家经济
    public void addPlayerVault(Player player, String lv) {
        if (economy == null) {
            System.out.println("Vault not setup");
            return;
        }
        double amount = Math.round(randomAmount(config.getMinVault(lv), config.getMaxVault(lv)) * 100.0 / 100.0);
        EconomyResponse resp = economy.depositPlayer(player, amount);
        if (resp.transactionSuccess()) {
            player.sendMessage(ChatColor.GREEN + "您因搜刮" +
                    ChatColor.GOLD + lv + "箱子" +
                    ChatColor.GREEN + "获得" +
                    ChatColor.DARK_PURPLE + amount +
                    ChatColor.GREEN + "金币");
        } else {
            player.sendMessage(ChatColor.RED + "添加金币失败！");
        }
    }
}