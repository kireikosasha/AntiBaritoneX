package kireiko.dev.antibaritonex.checks;

import kireiko.dev.antibaritonex.utils.Punish;
import kireiko.dev.antibaritonex.utils.RayLine;
import kireiko.dev.antibaritonex.utils.RayUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PatternCheck {
    private static final Map<Player, List<PlayerMoveEvent>> buffer = new ConcurrentHashMap<>();
    private static final List<String> patterns = Arrays.asList(
                    "532", "542"
    );
    public static void check(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!buffer.containsKey(player)) buffer.put(player, new LinkedList<>());
        buffer.get(player).add(event);
        if (buffer.get(player).size() >= 25) {
            analysis(player);
            buffer.get(player).clear();
        }
    }
    public static void analysis(Player player) {
        List<PlayerMoveEvent> moves = buffer.get(player);
        RayLine oldRayLine = null;
        int flagsRot = 0;
        double oldDeltaYaw = 0;
        double oldDeltaRotYaw = 0;
        StringBuilder pattern = new StringBuilder();
        for (PlayerMoveEvent event : moves) {
            Location to = event.getTo();
            Location from = event.getFrom();
            double
            dx = to.getX() - from.getX(),
            dz = to.getZ() - from.getZ();
            double
            xF = RayUtils.scaleVal(from.getX(), 5),
            yF = RayUtils.scaleVal(from.getY(), 5),
            zF = RayUtils.scaleVal(from.getZ(), 5);
            double
            xFf = RayUtils.scaleVal(xF - Math.floor(xF), 2),
            yFf = RayUtils.scaleVal(yF - Math.floor(yF), 2),
            zFf = RayUtils.scaleVal(zF - Math.floor(zF), 2);
            RayLine rayLine = new RayLine(dx, dz);
            if (oldRayLine == null) {
                oldRayLine = rayLine;
            }
            double deltaYaw = RayUtils.calculateRayLines(rayLine, oldRayLine);
            double deltaRotYaw = Math.abs(RayUtils.wrapYaw(to.getYaw()) - RayUtils.wrapYaw(from.getYaw()));
            double deltaRotPitch = Math.abs(to.getPitch() - from.getPitch());
            if (deltaRotYaw > 0 && deltaRotPitch > 0 && deltaRotYaw < 0.005 && deltaRotPitch < 0.005) {
                flagsRot++;
            }
            if (oldDeltaYaw == 0) {
                oldDeltaYaw = deltaYaw;
                oldDeltaRotYaw = deltaRotYaw;
            }
            double fucked = Math.round(Math.abs(deltaYaw - oldDeltaYaw));
            pattern.append((int) fucked);

            oldRayLine = rayLine;
            oldDeltaYaw = deltaYaw;
            oldDeltaRotYaw = deltaRotYaw;
        }
        if (flagsRot > 0) {
            Punish.use(player, flagsRot * 150, "Baritone", "Rotations");
        }
        String finalPattern = pattern.toString();
        for (String s : patterns) {
            if (finalPattern.contains(s)) {
                Punish.use(player, 65, "Baritone", "Analysis");
            }
        }
    }

    public static void clear(Player player) {
        buffer.remove(player);
    }

}
