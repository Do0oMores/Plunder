package top.mores.plunder.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlunderChest {

    public void createPlunderChest(Player player) {
        Inventory chest= Bukkit.createInventory(player, 18, "Plunder Chest");

    }
}
