package top.mores.plunder.EventListener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import top.mores.plunder.GUI.EditMainGUI;
import top.mores.plunder.GUI.LoadGUI;
import top.mores.plunder.Plunder;
import top.mores.plunder.Utils.ConfigUtil;
import top.mores.plunder.Utils.DataUtil;
import top.mores.plunder.Utils.ItemUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerListener implements Listener {

    ConfigUtil configUtil = new ConfigUtil();
    LoadGUI loadGUI = new LoadGUI();
    DataUtil dataUtil = new DataUtil();
    EditMainGUI editMainGUI = new EditMainGUI();

    /**
     * 监听玩家打开箱子事件
     * @param event 玩家点击事件
     */
    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (configUtil.onPlunderWorld(player.getWorld().getName()) &&
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.CHEST)) {
            event.setCancelled(true);
            Location loc = event.getClickedBlock().getLocation();

            // 检查箱子是否在冷却中
            long currentTime = System.currentTimeMillis();  // 当前时间（毫秒）
            long lastPlunderTime = dataUtil.getChestTime(loc);  // 获取上次搜刮的时间

            if (lastPlunderTime != 0) {
                long cooldown = configUtil.getPlunderTime() * 1000L;
                if (currentTime - lastPlunderTime < cooldown) {
                    player.sendMessage("§c箱子冷却中，请稍后再试！");
                    return;
                }
            }
            // 如果不在冷却中，则开始搜刮过程并保存箱子数据
            dataUtil.saveChestData(loc, currentTime);  // 记录新的搜刮时间
            loadGUI.createLoadGUI(player, loc);  // 打开加载界面
        }
    }

    /**
     * 监听玩家点击容器（箱子）内格子事件
     * @param event 容器点击事件
     */
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if (!title.equals("编辑搜刮物品")) {
            return;
        }
        int slot = event.getSlot();
        if (slot > configUtil.getChestLvList().size()) {
            event.setCancelled(true);
            return;
        }
        editMainGUI.editGUI(player, event.getSlot());
        event.setCancelled(true);
    }

    /**
     * 监听玩家关闭容器事件
     * @param event 容器关闭事件
     */
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        String title = event.getView().getTitle();

        if (!configUtil.getChestLvList().contains(title)) {
            return;
        }
        Inventory inventory = event.getInventory();
        List<Map<String, Object>> items = Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .map(ItemUtil::getItemStackMap)
                .collect(Collectors.toList());
        // 保存到配置文件中
        Plunder.getInstance().getConfig().set("箱子数据." + title, items);
        Plunder.getInstance().saveConfig();
    }

    /**
     * 监听玩家移动事件
     * @param event 玩家移动事件
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!configUtil.onPlunderWorld(player.getWorld().getName())) {
            return;
        }
        configUtil.playerLeave(player);
    }
}