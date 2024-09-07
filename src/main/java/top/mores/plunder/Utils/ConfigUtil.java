package top.mores.plunder.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import top.mores.plunder.Plunder;

import java.util.List;

public class ConfigUtil {

    FileConfiguration config = Plunder.getInstance().getConfig();

    private final List<String> allowPlunderWorlds = config.getStringList("允许搜刮的世界");

    public boolean onPlunderWorld(String world) {
        return allowPlunderWorlds.contains(world);
    }
}
