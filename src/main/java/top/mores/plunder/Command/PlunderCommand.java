package top.mores.plunder.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.mores.plunder.GUI.LoadGUI;

public class PlunderCommand implements CommandExecutor {
    LoadGUI loadGUI = new LoadGUI();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1 && strings[0].equalsIgnoreCase("loadtest")) {
                Player player = (Player) commandSender;
                loadGUI.createLoadGUI(player);
            }
        } else {
            commandSender.sendMessage("Only players can execute this command.");
        }
        return true;
    }
}
