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
        double maxSpeed = ElytraSpeedCap.getConfig().max_speed;

        for (ServerPlayer player : level.players()) {
            if (!player.isFallFlying())
                continue;

            // Cap the player's velocity
            Vec3 velocity = player.getDeltaMovement();
            Vec3 capped = ElytraSpeedCap.capElytraVelocity(maxSpeed, velocity);

            // Check if velocity was capped
            if (capped != null) {
                player.setDeltaMovement(capped);

                // Tell the client "your motion changed", so it updates cleanly
                player.hasImpulse = true;
                player.hurtMarked = true;
            }
        }
    }
}
