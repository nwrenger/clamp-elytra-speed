package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.Packets;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraSpeedCap implements ModInitializer {

    public static final String MOD_ID = "elytra-speed-cap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ElytraSpeedCapConfig config = null;

    public static ElytraSpeedCapConfig getConfig() {
        return Objects.requireNonNull(config);
    }

    @Override
    public void onInitialize() {
        config = ElytraSpeedCapConfig.load();

        ElytraSpeedHandler.init();

        Packets.register();
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> Packets.syncMaxSpeed(handler.getPlayer(), getConfig().max_speed));

        LOGGER.info("[Elytra Speed Cap] Started successfully");
    }

    @Nullable
    public static final Vec3 capElytraVelocity(double maxSpeed, Vec3 velocity) {
        // Convert max speed from config units to per-tick units
        double maxSpeedPerTick = ElytraSpeedCapConfig.intoMaxSpeedPerTick(maxSpeed);
        if (maxSpeedPerTick <= 0.0D)
            return null;

        // Calculate horizontal speed
        double horizontal = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        if (horizontal <= maxSpeedPerTick)
            return null;

        // Scale velocity to cap horizontal speed
        double factor = maxSpeedPerTick / horizontal;
        Vec3 capped = velocity.scale(factor);

        LOGGER.debug(
                "[Elytra Speed Cap] Capping Elytra velocity from {} to {} (factor={}) oldVel={} newVel={}",
                horizontal,
                maxSpeedPerTick,
                factor,
                velocity,
                capped);

        return capped;
    }
}
