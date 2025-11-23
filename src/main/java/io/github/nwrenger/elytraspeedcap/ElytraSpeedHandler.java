package io.github.nwrenger.elytraspeedcap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class ElytraSpeedHandler {

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(ElytraSpeedHandler::onEndWorldTick);
    }

    private static void onEndWorldTick(ServerLevel level) {
        double maxSpeedPerTick = ElytraSpeedCapConfig.intoMaxSpeedPerTick(
                ElytraSpeedCap.getConfig().max_speed);
        if (maxSpeedPerTick <= 0.0D)
            return;

        for (ServerPlayer player : level.players()) {
            if (!player.isFallFlying())
                continue;

            Vec3 velocity = player.getDeltaMovement();

            // Horizontal speed from velocity, not from position delta
            double horizontal = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

            if (horizontal > maxSpeedPerTick) {
                double factor = maxSpeedPerTick / horizontal;

                // Apply cap
                Vec3 capped = velocity.scale(factor);
                player.setDeltaMovement(capped);

                // Tell the client "your motion changed", so it updates cleanly
                player.hasImpulse = true;
                player.hurtMarked = true;

                ElytraSpeedCap.LOGGER.debug(
                        "[Elytra Speed Cap] Soft-capped Elytra velocity for {}: horizontal={} max={}",
                        player.getGameProfile().name(),
                        horizontal,
                        maxSpeedPerTick);
            }
        }
    }
}
