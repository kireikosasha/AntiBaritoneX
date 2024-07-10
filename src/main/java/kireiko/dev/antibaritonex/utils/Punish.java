package kireiko.dev.antibaritonex.utils;

import kireiko.dev.antibaritonex.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Punish {
    private static final Map<Player, Integer> vl = new ConcurrentHashMap<>();
    private static final Map<Player, Integer> count = new ConcurrentHashMap<>();
    public static void use(Player player, int vlAdd, String reason, String type) {
        vl.put(player, vl.get(player) + vlAdd);
        if (vl.get(player) >= 100) {
            count.put(player, count.get(player) + 1);
            String msg = Wrappers.wrapAll(Main.getInstance().getConfig().getString("message"),
                            player, reason, type, vl.get(player), count.get(player));
            String console = Wrappers.wrapAll(Main.getInstance().getConfig().getString("console"),
                            player, reason, type, vl.get(player), count.get(player));
            Wrappers.sendMessagesToPlayers("abx.moder", msg);
            Wrappers.sendMessageConsole(console);
            if (count.get(player) >= Main.getInstance().getConfig().getInt("max-count")) {
                String punish = Wrappers.wrapAll(Main.getInstance().getConfig().getString("punish"),
                                player, reason, type, vl.get(player), count.get(player));
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), punish);
                });
            }
        }
    }
    public static void fade(Player player) {
        if (vl.get(player) > 0) {
            vl.put(player, vl.get(player) - 1);
        }
    }

    public static void add(Player player) {
        vl.put(player, 0);
        count.put(player, 0);
    }

    public static void clear(Player player) {
        vl.remove(player);
        count.remove(player);
    }
}
