package top.mores.plunder;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.mores.plunder.Command.PlunderCommand;
import top.mores.plunder.EventListener.PlayerListener;
import top.mores.plunder.Utils.VaultUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class Plunder extends JavaPlugin {

    private static Plunder instance;
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration data;
    private File dataFile;

    @Override
    public void onEnable() {
        instance = this;
        if (!VaultUtil.setupEconomy()) {
            getLogger().severe("Vault plugin not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        initFiles();
        Objects.requireNonNull(getCommand("plunder")).setExecutor(new PlunderCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("Plunder has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plunder has been disabled!");
    }

    //获取插件实例
    public static Plunder getInstance() {
        return instance;
    }

    //获取config.yml
    @Override
    @NotNull
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    //获取data
    public FileConfiguration getData() {
        if (data == null) {
            reloadData();
        }
        return data;
    }

    public void reloadConfig() {
        config= YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadData() {
        data= YamlConfiguration.loadConfiguration(dataFile);
    }

    //保存data
    public void saveData(){
        try{
            data.save(dataFile);
        }catch (IOException e){
            getLogger().severe("Failed to save data!");
        }
    }

    private void initFiles() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            boolean created = configFile.getParentFile().mkdirs();
            if (!created) {
                getLogger().severe("Failed to create config.yml!");
                return;
            }
            saveResource("config.yml", false);
        }
        reloadConfig();

        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            }catch (IOException e) {
                getLogger().severe("Failed to create data.yml!");
            }
        }
        reloadData();
    }
}