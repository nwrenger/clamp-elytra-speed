package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.Packets;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
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
}
