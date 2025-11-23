package io.github.nwrenger.elytraspeedcap.network;

import io.github.nwrenger.elytraspeedcap.ElytraSpeedCap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncMaxSpeedPayload(double maxSpeed) implements CustomPacketPayload {

    // Vanilla packet ID (example-mod:sync_max_speed)
    public static final ResourceLocation SYNC_MAX_SPEED_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(
            ElytraSpeedCap.MOD_ID,
            "sync_max_speed");

    // Fabric / MC payload type
    public static final CustomPacketPayload.Type<SyncMaxSpeedPayload> ID = new CustomPacketPayload.Type<>(
            SYNC_MAX_SPEED_PAYLOAD_ID);

    // How to (de)serialize this payload
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMaxSpeedPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, // write/read a double
            SyncMaxSpeedPayload::maxSpeed,
            SyncMaxSpeedPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
