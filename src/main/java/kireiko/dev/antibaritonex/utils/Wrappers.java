package kireiko.dev.antibaritonex.utils;

import kireiko.dev.antibaritonex.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Wrappers {
    public static String wrapColors(String text) { return text.replace("&", "§"); }
    public static String wrapAll(String text, Player player, String check, String module, int localVl, int count) {
        return wrapColors(text.replace("%player%", player.getName())
                        .replace("%check%", check)
                        .replace("%check-module%", module)
                        .replace("%count%", String.valueOf(count))
                        .replace("%vl%", String.valueOf(localVl)));
    }
    public static void sendMessagesToPlayers(String permission, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
    public static void sendMessageConsole(String message) {
        Main.getInstance().getLogger().warning(message);
    }
}
