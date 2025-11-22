package io.github.nwrenger.clampelytraspeed;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ElytraClampHandler {
    // Last server-side position per player
    private static final Map<UUID, Vec3> LAST_POS = new HashMap<>();

    // How far over the cap we allow before correcting (1.05 = 5% over)
    private static final double SOFT_CAP_FACTOR = 1.05;

    // How strongly we pull toward the clamped position each tick (0..1)
    private static final double BLEND_FACTOR = 0.4;

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(ElytraClampHandler::onEndWorldTick);
    }

    private static void onEndWorldTick(ServerLevel level) {
        double maxSpeedPerTick = ClampElytraSpeedConfig.intoMaxSpeedPerTick(ClampElytraSpeed.getConfig().max_speed);
        if (maxSpeedPerTick <= 0.0D) return;

        double softCap = maxSpeedPerTick * SOFT_CAP_FACTOR;

        for (ServerPlayer player : level.players()) {
            UUID id = player.getUUID();

            if (!player.isFallFlying()) {
                LAST_POS.remove(id);
                continue;
            }

            Vec3 current = player.position();
            Vec3 previous = LAST_POS.get(id);

            if (previous != null) {
                Vec3 delta = current.subtract(previous);

                // Horizontal distance moved this tick
                double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);

                // Only start correcting once we're *over* the soft cap
                if (horizontal > softCap) {
                    // Target: what the displacement *would* be at exactly maxSpeedPerTick
                    double factorToMax = maxSpeedPerTick / horizontal;

                    Vec3 clampedDelta = new Vec3(
                            delta.x * factorToMax,
                            delta.y * factorToMax, // or delta.y if you only want horizontal clamping
                            delta.z * factorToMax
                    );

                    Vec3 targetPos = previous.add(clampedDelta);

                    // Blend current position towards target position
                    // blended = current + (target - current) * BLEND_FACTOR
                    Vec3 offsetToTarget = targetPos.subtract(current);
                    Vec3 blended = current.add(offsetToTarget.scale(BLEND_FACTOR));

                    player.teleportTo(blended.x, blended.y, blended.z);

                    ClampElytraSpeed.LOGGER.debug(
                            "[Clamp Elytra Speed] Soft-clamped Elytra move for {}: horizontal={} softCap={} max={} blend={}",
                            player.getGameProfile().name(),
                            horizontal,
                            softCap,
                            maxSpeedPerTick,
                            BLEND_FACTOR
                    );

                    current = blended;
                }
            }

            LAST_POS.put(id, player.position());
        }
    }
}
