package top.mores.plunder.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import top.mores.plunder.Plunder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public ItemStack[] getItemsFromChest(String chestName) {
        FileConfiguration config = Plunder.getInstance().getConfig();
        String path = "箱子数据." + chestName;
        List<Map<String, Object>> list = config.contains(path) ? (List<Map<String, Object>>) config.getList(path) : null;
        return (list != null ? ItemUtil.getItemStacksFromConfig(list) : new ItemStack[0]);
    }

    public List<String> getSuperLvList(){
        return config.getStringList("箱子数据.公告箱子品阶");
    }
}
