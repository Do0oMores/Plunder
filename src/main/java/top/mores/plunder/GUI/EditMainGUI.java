package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mores.plunder.Utils.ConfigUtil;

public class EditMainGUI {

    ConfigUtil config = new ConfigUtil();

    public void createEditMainGUI(Player player) {
        Inventory main = Bukkit.createInventory(player, 9, "编辑搜刮物品");
        System.out.println(config.getChestLvList());
        for (int i = 0; i < config.getChestLvList().size(); i++) {
            String name = config.getChestLvList().get(i);

            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta meta = chest.getItemMeta();

            if (meta != null) {
                // 设置箱子的自定义名称
                meta.setDisplayName("§6" + name + " 箱子");
                chest.setItemMeta(meta);
            }

            main.setItem(i, chest);
        }
        player.openInventory(main);
    }
}

