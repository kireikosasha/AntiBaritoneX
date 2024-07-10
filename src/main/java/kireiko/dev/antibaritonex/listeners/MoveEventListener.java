package kireiko.dev.antibaritonex.listeners;

import kireiko.dev.antibaritonex.checks.PatternCheck;
import kireiko.dev.antibaritonex.utils.Punish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void move(PlayerMoveEvent event) {
        PatternCheck.check(event);
        Punish.fade(event.getPlayer());
    }
}
