package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.mores.plunder.Plunder;
import top.mores.plunder.Utils.DataUtil;

public class LoadGUI {

    DataUtil data = new DataUtil();
    PlunderChest plunderChest = new PlunderChest();

    /**
     * 创建搜刮界面
     * @param player 打开搜刮界面的玩家
     * @param loc 搜刮箱子的坐标
     */
    public void createLoadGUI(Player player, Location loc) {
        Inventory inventory = Bukkit.createInventory(player, 9, "§c搜刮中...");
        // 初始化GUI
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, grayGlass);
        }

        player.openInventory(inventory);
        // 开始5秒的进度加载
        final int[] progress = {0};

        Bukkit.getScheduler().runTaskTimer(Plunder.getInstance(), task -> {
            if (progress[0] < 9) {
                // 将对应的玻璃板从灰色变成绿色
                ItemStack greenGlass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                inventory.setItem(progress[0], greenGlass);
                progress[0]++;
            } else if (progress[0] == 9) {
                data.saveChestData(loc, System.currentTimeMillis());
                plunderChest.createPlunderChest(player);
                task.cancel(); // 停止当前任务
            }
        }, 0L, 10L);
    }
}