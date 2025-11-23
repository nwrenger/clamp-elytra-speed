package io.github.nwrenger.elytraspeedcap.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class Packets {

    private Packets() {
    }

    public static void register() {
        // Must be called on BOTH physical sides during common init
        PayloadTypeRegistry.playS2C().register(
                SyncMaxSpeedPayload.ID,
                SyncMaxSpeedPayload.CODEC);
    }

    public static void syncMaxSpeed(ServerPlayer player, double maxSpeed) {
        ServerPlayNetworking.send(player, new SyncMaxSpeedPayload(maxSpeed));
    }
}
