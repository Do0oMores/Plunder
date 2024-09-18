package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mores.plunder.Plunder;
import top.mores.plunder.Utils.ConfigUtil;
import top.mores.plunder.Utils.ItemUtil;

import java.util.List;
import java.util.Map;

public class EditMainGUI {

    ConfigUtil config = new ConfigUtil();
    List<String> chestLvList = config.getChestLvList();

    /**
     * 创建编辑搜刮物品主界面
     * @param player 打开主界面的玩家
     */
    public void createEditMainGUI(Player player) {
        Inventory main = Bukkit.createInventory(player, 9, "编辑搜刮物品");
        for (int i = 0; i < chestLvList.size(); i++) {
            String name = chestLvList.get(i);

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

    /**
     * 创建并打开编辑界面
     * @param player 打开编辑界面的玩家
     * @param slot 点击了编辑主界面的哪个格子
     */
    public void editGUI(Player player, int slot) {
        Inventory GUI = Bukkit.createInventory(player, 18, chestLvList.get(slot));
        // 获取对应槽位的物品列表
        ItemStack[] items = GUIItems(slot);
        // 将每个物品放入GUI中对应的位置
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                GUI.setItem(i, items[i]);
            }
        }
        player.openInventory(GUI);
    }

    /**
     * 编辑界面的物品
     * @param slot 对应编辑主界面的格子数
     * @return 物品组
     */
    public ItemStack[] GUIItems(int slot) {
        FileConfiguration config = Plunder.getInstance().getConfig();
        String path = "箱子数据." + chestLvList.get(slot);
        List<Map<String, Object>> list = config.contains(path) ?
                (List<Map<String, Object>>) config.getList(path) : null;
        return (list != null ? ItemUtil.getItemStacksFromConfig(list) : new ItemStack[0]);
    }
}