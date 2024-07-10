package kireiko.dev.antibaritonex;

import kireiko.dev.antibaritonex.listeners.Cleaner;
import kireiko.dev.antibaritonex.listeners.MoveEventListener;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String VERSION = "1.0";

    @Getter(AccessLevel.PUBLIC)
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        PluginManager m = Bukkit.getPluginManager();
        {
            m.registerEvents(new MoveEventListener(), this);
            m.registerEvents(new Cleaner(), this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
