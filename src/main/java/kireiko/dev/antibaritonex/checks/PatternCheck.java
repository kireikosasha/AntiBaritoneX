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
                    "532", "542", "7432"
    );
    private static final List<String> invalidValues = Arrays.asList(
                    "9.0E-4", "6.0E-4", "7.0E-4"
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
        int flagsInv = 0;
        String invSens = "";
        double oldDeltaYaw = 0;
        double oldDeltaRotYaw = 0;
        double oldDeltaRotPitch = 0;
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
            if (oldDeltaYaw == 0) {
                oldDeltaYaw = deltaYaw;
                oldDeltaRotYaw = deltaRotYaw;
                oldDeltaRotPitch = deltaRotPitch;
            }
            double fucked = Math.round(Math.abs(deltaYaw - oldDeltaYaw));
            double fucked2 = RayUtils.scaleVal(deltaRotYaw, 4);
            String invalidCheck = String.valueOf(fucked2);
            pattern.append((int) fucked);
            if (invalidValues.contains(invalidCheck) && deltaRotPitch < 0.000000001D) {
                invSens = invalidCheck;
                flagsInv++;
            }

            oldRayLine = rayLine;
            oldDeltaYaw = deltaYaw;
            oldDeltaRotYaw = deltaRotYaw;
            oldDeltaRotPitch = deltaRotPitch;
        }
        if (flagsInv > 0) {
            Punish.use(player, 110, "Baritone", "Predicted " + invSens);
        }
        String finalPattern = pattern.toString();
        for (String s : patterns) {
            if (finalPattern.contains(s)) {
                Punish.use(player, 65, "Baritone", "Spike on 25x Table #" + s);
            }
        }
    }

    public static void clear(Player player) {
        buffer.remove(player);
    }

}
