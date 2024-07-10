package kireiko.dev.antibaritonex.listeners;

import kireiko.dev.antibaritonex.checks.PatternCheck;
import kireiko.dev.antibaritonex.utils.Punish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Cleaner implements Listener {

    @EventHandler
    public void bye(PlayerQuitEvent event) {
        Punish.clear(event.getPlayer());
        PatternCheck.clear(event.getPlayer());
    }


    @EventHandler
    public void hello(PlayerJoinEvent event) {
        Punish.add(event.getPlayer());
    }
}
