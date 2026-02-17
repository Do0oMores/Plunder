package top.mores.plunder.GUI;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.mores.plunder.Utils.ConfigUtil;
import top.mores.plunder.Utils.ItemUtil;
import top.mores.plunder.Utils.ProbabilityRandomizerUtil;
import top.mores.plunder.Utils.VaultUtil;

import java.util.*;

public class PlunderChest {

    ProbabilityRandomizerUtil probabilityRandomizerUtil = new ProbabilityRandomizerUtil();
    ConfigUtil configUtil = new ConfigUtil();
    VaultUtil vaultUtil = new VaultUtil();

    /**
     * 创建搜刮箱子
     *
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
        if (items != null && items.length > 0) {
            // 将随机选中的物品依次放入箱子
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    chest.setItem(i, items[i]);
                }
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "该品阶箱子未配置有效掉落物，当前为空箱。");
        }
        player.openInventory(chest);
    }

    public ItemStack[] getRandomItemsFromChest(String chestName, int itemCount) {
        List<Map<String, Object>> itemData = configUtil.getItemData(chestName);
        if (itemData == null || itemData.isEmpty() || itemCount <= 0) {
            return new ItemStack[0];
        }

        List<Map<String, Object>> pool = new ArrayList<>();
        for (Map<String, Object> item : itemData) {
            int weight = ((Number) item.getOrDefault("weight", 0)).intValue();
            if (weight > 0) {
                pool.add(item);
            }
        }

        if (pool.isEmpty()) {
            return new ItemStack[0];
        }

        List<ItemStack> selectedItems = new ArrayList<>();
        Random random = new Random();

        while (!pool.isEmpty() && selectedItems.size() < itemCount) {
            int totalWeight = 0;
            for (Map<String, Object> item : pool) {
                totalWeight += ((Number) item.get("weight")).intValue();
            }

            int r = random.nextInt(totalWeight);
            int acc = 0;
            int chosenIndex = -1;

            for (int i = 0; i < pool.size(); i++) {
                acc += ((Number) pool.get(i).get("weight")).intValue();
                if (r < acc) {
                    chosenIndex = i;
                    break;
                }
            }

            if (chosenIndex < 0) break;

            Map<String, Object> chosen = pool.get(chosenIndex);
            ItemStack item = ItemUtil
                    .getItemStacksFromConfig(Collections.singletonList(chosen))[0];

            pool.remove(chosenIndex);

            if (item != null && item.getType() != Material.AIR) {
                selectedItems.add(item);
            }
        }

        return selectedItems.toArray(new ItemStack[0]);
    }

}