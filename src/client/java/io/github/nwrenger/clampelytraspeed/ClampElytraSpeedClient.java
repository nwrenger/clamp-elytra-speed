package io.github.nwrenger.clampelytraspeed;

import io.github.nwrenger.clampelytraspeed.network.SyncMaxSpeedPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class ClampElytraSpeedClient implements ClientModInitializer {
    public static volatile double maxSpeed = 0.0D;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                SyncMaxSpeedPayload.ID,
                (payload, context) -> {
                    double max = payload.maxSpeed();
                    context.client().execute(() -> maxSpeed = max);
                });
    }
}
