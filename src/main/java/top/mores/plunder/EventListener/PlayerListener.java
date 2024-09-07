package top.mores.plunder.EventListener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import top.mores.plunder.GUI.LoadGUI;
import top.mores.plunder.Utils.ConfigUtil;

import java.util.Objects;

public class PlayerListener implements Listener {
    ConfigUtil configUtil = new ConfigUtil();
    LoadGUI loadGUI = new LoadGUI();

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (configUtil.onPlunderWorld(player.getWorld().getName()) &&
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.CHEST)) {
            event.setCancelled(true);
            loadGUI.createLoadGUI(player);
        }
    }
}