package top.mores.plunder.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import top.mores.plunder.Plunder;

import java.util.*;

public class ConfigUtil {

    FileConfiguration config = Plunder.getInstance().getConfig();

    private List<String> allowPlunderWorlds() {
        return config.getStringList("允许搜刮的世界");
    }

    private final Map<UUID, BukkitTask> countdownTasks = new HashMap<>();

    /**
     * 是否在允许搜刮的世界内
     *
     * @param world 世界名
     * @return boolean
     */
    public boolean onPlunderWorld(String world) {
        return allowPlunderWorlds().contains(world);
    }

    public long getPlunderTime() {
        return config.getLong("箱子搜刮间隔", 600L);
    }

    public List<String> getChestLvList() {
        return new ArrayList<>(Objects.requireNonNull(config.getConfigurationSection("箱子数据")).getKeys(false));
    }

    public ItemStack[] getItemsFromChest(String chestName) {
        FileConfiguration config = Plunder.getInstance().getConfig();
        String path = "箱子数据." + chestName;
        List<Map<String, Object>> list = config.contains(path) ? (List<Map<String, Object>>) config.getList(path) : null;
        return (list != null ? ItemUtil.getItemStacksFromConfig(list) : new ItemStack[0]);
    }

    public List<String> getSuperLvList() {
        return config.getStringList("公告箱子品阶");
    }

    public int getLeaveScope() {
        return config.getInt("撤离范围");
    }

    private World leaveWorld() {
        return Bukkit.getWorld(Objects.requireNonNull(config.getString("撤离传送点.world")));
    }
    private double leavePointX(){
        return config.getDouble("撤离传送点.x");
    }
    private double leavePointY(){
        return config.getDouble("撤离传送点.y");
    }
    private double leavePointZ(){
        return config.getDouble("撤离传送点.z");
    }
    private int countdownTime(){
        return config.getInt("撤离时间");
    }

    public int getMinVault(String lv){
        return config.getInt("箱子金币奖励."+lv+".min");
    }

    public int getMaxVault(String lv){
        return config.getInt("箱子金币奖励."+lv+".max");
    }

    public void playerLeave(Player player) {
        Location loc = player.getLocation();
        UUID playerUUID = player.getUniqueId();

        for (String locKey : Objects.requireNonNull(config.getConfigurationSection("撤离点")).getKeys(false)) {
            // 获取每个撤离点的坐标和世界
            String worldName = config.getString("撤离点." + locKey + ".world");
            double locX = config.getDouble("撤离点." + locKey + ".x");
            double locY = config.getDouble("撤离点." + locKey + ".y");
            double locZ = config.getDouble("撤离点." + locKey + ".z");

            if (!Objects.requireNonNull(loc.getWorld()).getName().equals(worldName)) {
                continue;
            }
            Location evacuateLoc = new Location(loc.getWorld(), locX, locY, locZ);
            double distance = loc.distance(evacuateLoc);
            // 如果玩家进入撤离范围
            if (distance <= getLeaveScope()) {
                if (!countdownTasks.containsKey(playerUUID)) {
                    player.sendTitle(ChatColor.GREEN + "进入撤离范围", ChatColor.YELLOW + locKey, 10, 70, 20);
                    // 开始10秒倒计时
                    startCountdown(player, locKey);
                }
                return;
            }
        }
        // 如果玩家不在撤离范围内，取消倒计时并移除任务
        if (countdownTasks.containsKey(playerUUID)) {
            countdownTasks.get(playerUUID).cancel();
            countdownTasks.remove(playerUUID);
            player.sendTitle(ChatColor.RED + "你已离开撤离范围", "", 10, 70, 20);
        }
    }

    public void startCountdown(Player player, String locKey) {
        UUID playerUUID = player.getUniqueId();
        // 如果已经有倒计时任务在运行，先取消之前的任务，重置倒计时
        if (countdownTasks.containsKey(playerUUID)) {
            BukkitTask existingTask = countdownTasks.get(playerUUID);
            if (existingTask != null) {
                existingTask.cancel(); // 取消当前倒计时任务
            }
            countdownTasks.remove(playerUUID); // 移除旧任务记录
        }
        BukkitRunnable countdownRunnable = new BukkitRunnable() {
            int timeLeft = countdownTime();
            @Override
            public void run() {
                if (timeLeft > 0) {
                    player.sendTitle(
                            ChatColor.GREEN + "将在 " + timeLeft + " 秒后撤离",
                            ChatColor.YELLOW + "请保持位置",
                            10, 20, 10
                    );
                } else {
                    // 清除倒计时的 Title
                    player.sendTitle("", "", 0, 0, 0);
                    // 执行倒计时结束后的逻辑
                    executeNextStep(player);
                    // 取消任务并移除记录
                    this.cancel();
                    countdownTasks.remove(playerUUID);
                    return;
                }
                timeLeft--;
            }
        };
        // 启动倒计时任务，每秒执行一次
        BukkitTask task = countdownRunnable.runTaskTimer(Plunder.getInstance(), 0L, 20L);
        // 将倒计时任务存储在 map 中
        countdownTasks.put(playerUUID, task);
    }

    private void executeNextStep(Player player) {
        UUID playerUUID = player.getUniqueId();
        // 确保倒计时结束后不会重复执行
        if (!countdownTasks.containsKey(playerUUID)) {
            return;
        }
        // 执行倒计时结束后的传送逻辑
        player.sendMessage(ChatColor.GOLD + "撤离成功！");
        if (leaveWorld() != null) {
            Location loc = new Location(leaveWorld(), leavePointX(), leavePointY(), leavePointZ());
            player.teleport(loc);
            // 取消任务和移除记录以防止重复执行
            countdownTasks.remove(playerUUID);
        } else {
            player.sendMessage(ChatColor.RED + "无法找到指定的世界");
        }
    }
}