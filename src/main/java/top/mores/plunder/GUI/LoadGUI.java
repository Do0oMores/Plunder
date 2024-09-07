package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.mores.plunder.Plunder;

public class LoadGUI {

    public void createLoadGUI(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 9, "§c搜刮中...");

        // 初始化GUI，放置灰色玻璃板表示总进程
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, grayGlass);
        }

        player.openInventory(inventory);

        // 开始10秒的进度加载
        final int[] progress = {0};

        Bukkit.getScheduler().runTaskTimer(Plunder.getInstance(), task -> {
            if (progress[0] < 9) {
                // 将对应的玻璃板从灰色变成绿色
                ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                inventory.setItem(progress[0], greenGlass);
                progress[0]++;
            } else if (progress[0] == 9) {
                // 所有格子加载完成后，停顿1秒进行下一个操作

                Bukkit.getScheduler().runTaskLater(Plunder.getInstance(), player::closeInventory, 20L);
                task.cancel(); // 停止当前任务
            }
        }, 0L, 20L);
    }
}
