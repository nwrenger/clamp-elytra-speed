package io.github.nwrenger.clampelytraspeed;

import io.github.nwrenger.clampelytraspeed.network.Packets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClampElytraSpeed implements ModInitializer {
    public static final String MOD_ID = "clamp-elytra-speed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ClampElytraSpeedConfig config = null;

    public static ClampElytraSpeedConfig getConfig() {
        return Objects.requireNonNull(config);
    }

    @Override
    public void onInitialize() {
        config = ClampElytraSpeedConfig.load();

        ElytraClampHandler.init();

        Packets.register();
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> Packets.syncMaxSpeed(handler.getPlayer(), getConfig().max_speed));

        LOGGER.info("[Clamp Elytra Speed] Started successfully");
    }
}
