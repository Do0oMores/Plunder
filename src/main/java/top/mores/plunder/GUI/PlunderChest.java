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
        if (items != null) {
            // 将随机选中的物品依次放入箱子
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    chest.setItem(i, items[i]);
                }
            }
        } else {
            player.sendMessage("你打开了一个空的搜刮箱");
        }
        player.openInventory(chest);
    }

    public ItemStack[] getRandomItemsFromChest(String chestName, int itemCount) {
        List<Map<String, Object>> itemData = configUtil.getItemData(chestName);
        List<ItemStack> selectedItems = new ArrayList<>();
        Random random = new Random();

        if (itemData != null && !itemData.isEmpty()) {
            while (selectedItems.size() < itemCount) {
                int randomValue = random.nextInt(100);
                boolean itemSelected = false;

                for (Map<String, Object> item : itemData) {
                    if (item.containsKey("weight")) {
                        int weight = (int) item.get("weight");
                        if (weight > randomValue) {
                            ItemStack selectedItem = ItemUtil.getItemStacksFromConfig(Collections.singletonList(item))[0];
                            // 检查是否已选择该物品
                            if (!selectedItems.contains(selectedItem)) {
                                selectedItems.add(selectedItem);
                                itemSelected = true;
                                break; // 选中后退出内层循环
                            }
                        }
                    }
                }
                // 如果没有选中任何物品，退出循环，避免无限循环
                if (!itemSelected) {
                    break;
                }
            }
            return selectedItems.toArray(new ItemStack[0]);
        } else {
            return null;
        }
    }
}