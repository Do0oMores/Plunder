package top.mores.plunder.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.mores.plunder.Plunder;

public class PlunderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
                Player player = (Player) commandSender;
                if (player.isOp()) {
                    Plunder.getInstance().reloadConfig();
                    Plunder.getInstance().reloadData();
                    player.sendMessage("配置文件已重载");
                }
            }
        } else {
            commandSender.sendMessage("Only players can execute this command.");
        }
        return true;
    }
}
