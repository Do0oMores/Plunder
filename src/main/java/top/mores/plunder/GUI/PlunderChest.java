package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.mores.plunder.Utils.ConfigUtil;
import top.mores.plunder.Utils.ProbabilityRandomizerUtil;
import top.mores.plunder.Utils.VaultUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlunderChest {

    ProbabilityRandomizerUtil probabilityRandomizerUtil = new ProbabilityRandomizerUtil();
    ConfigUtil configUtil = new ConfigUtil();
    VaultUtil vaultUtil = new VaultUtil();

    /**
     * 创建搜刮箱子
     * @param player 打开搜刮箱子的玩家
     */
    public void createPlunderChest(Player player) {
        String chestName = probabilityRandomizerUtil.getRandomRarity();
        vaultUtil.addPlayerVault(player, chestName);
        if (configUtil.getSuperLvList().contains(chestName)) {
            Location loc = player.getLocation();
            for (Player p : player.getWorld().getPlayers()) {
                p.sendMessage(ChatColor.DARK_AQUA + "【NOTE！】" +
                        ChatColor.GOLD + player.getName() +
                        ChatColor.BLUE + "在 " +
                        ChatColor.GREEN + Math.round(loc.getX()) + "," + Math.round(loc.getY()) + "," + Math.round(loc.getZ()) +
                        ChatColor.BLUE + " 开出了" +
                        ChatColor.RED + chestName + "宝箱");
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        Inventory chest = Bukkit.createInventory(player, 18, ChatColor.GOLD + chestName + "箱子");

        Random random = new Random();
        int randomItemCount = random.nextInt(5) + 1;
        ItemStack[] items = getRandomItemsFromChest(chestName, randomItemCount);
        // 将随机选中的物品依次放入箱子
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                chest.setItem(i, items[i]);
            }
        }
        player.openInventory(chest);
    }

    /**
     * 加载随机的物品组放入搜刮箱中
     * @param chestName 箱子名
     * @param itemCount 物品数
     * @return 物品组
     */
    public ItemStack[] getRandomItemsFromChest(String chestName, int itemCount) {
        // 获取该品阶对应的物品列表
        ItemStack[] allItems = configUtil.getItemsFromChest(chestName);
        List<ItemStack> itemList = new ArrayList<>(Arrays.asList(allItems));
        List<ItemStack> selectedItems = new ArrayList<>();

        itemCount = Math.min(itemCount, itemList.size());
        Random random = new Random();
        for (int i = 0; i < itemCount; i++) {
            int randomIndex = random.nextInt(itemList.size());
            ItemStack item = itemList.remove(randomIndex).clone();
            selectedItems.add(item);
        }
        return selectedItems.toArray(new ItemStack[0]);
    }
}