package top.mores.plunder.Utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import top.mores.plunder.Plunder;

public class DataUtil {

    FileConfiguration data = Plunder.getInstance().getData();

    // 保存箱子数据：如果存在相同坐标的箱子，更新其时间；如果没有，创建新的箱子数据
    public void saveChestData(Location loc, long time) {
        String locKey = findExistingLocKey(loc);

        if (locKey != null) {
            // 如果找到了相同坐标的箱子，更新其 time
            data.set(locKey + ".time", time);
        } else {
            // 否则，保存新的箱子数据
            int locIndex = getNextLocIndex();  // 获取下一个可用的 loc 编号
            locKey = "loc" + locIndex;
            data.set(locKey + ".x", loc.getX());
            data.set(locKey + ".y", loc.getY());
            data.set(locKey + ".z", loc.getZ());
            data.set(locKey + ".time", time);
        }
        // 保存配置数据
        Plunder.getInstance().saveData();
    }

    // 获取箱子上次的搜刮时间，如果不存在则返回 0
    public long getChestTime(Location loc) {
        String locKey = findExistingLocKey(loc);

        if (locKey != null) {
            return data.getLong(locKey + ".time");
        }
        return 0;  // 没有找到对应的箱子
    }

    // 查找已有箱子数据中是否有匹配的坐标，返回 locKey
    private String findExistingLocKey(Location loc) {
        for (String key : data.getKeys(false)) {
            if (key.startsWith("loc")) {
                double x = data.getDouble(key + ".x");
                double y = data.getDouble(key + ".y");
                double z = data.getDouble(key + ".z");

                if (x == loc.getX() && y == loc.getY() && z == loc.getZ()) {
                    return key;  // 找到匹配的坐标，返回 locKey
                }
            }
        }
        return null;  // 没有找到匹配的坐标
    }

    // 计算下一个 loc 编号
    private int getNextLocIndex() {
        int maxIndex = 0;
        // 遍历现有的所有键，查找最大的 loc 编号
        for (String key : data.getKeys(false)) {
            if (key.startsWith("loc")) {
                try {
                    // 提取 loc 编号并进行比较
                    int index = Integer.parseInt(key.replace("loc", ""));
                    if (index > maxIndex) {
                        maxIndex = index;
                    }
                } catch (NumberFormatException ignored) {
                    // 如果无法解析编号，跳过此键
                }
            }
        }
        // 返回下一个可用的 loc 编号
        return maxIndex + 1;
    }
}