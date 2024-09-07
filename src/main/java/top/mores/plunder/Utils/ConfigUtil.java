package top.mores.plunder.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import top.mores.plunder.Plunder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigUtil {

    FileConfiguration config = Plunder.getInstance().getConfig();

    private final List<String> allowPlunderWorlds = config.getStringList("允许搜刮的世界");

    public boolean onPlunderWorld(String world) {
        return allowPlunderWorlds.contains(world);
    }

    public long getPlunderTime(){
        return config.getLong("箱子搜刮间隔",600L);
    }

    public List<String> getChestLvList(){
        return new ArrayList<>(Objects.requireNonNull(config.getConfigurationSection("箱子数据")).getKeys(false));
    }
}
