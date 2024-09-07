package top.mores.plunder.EventListener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import top.mores.plunder.GUI.LoadGUI;
import top.mores.plunder.Utils.ConfigUtil;
import top.mores.plunder.Utils.DataUtil;

import java.util.Objects;

public class PlayerListener implements Listener {

    ConfigUtil configUtil = new ConfigUtil();
    LoadGUI loadGUI = new LoadGUI();
    DataUtil dataUtil = new DataUtil();

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
}