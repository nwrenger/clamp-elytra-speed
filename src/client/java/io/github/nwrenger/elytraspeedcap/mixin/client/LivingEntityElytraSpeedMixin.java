package io.github.nwrenger.elytraspeedcap.mixin.client;

import io.github.nwrenger.elytraspeedcap.ElytraSpeedCap;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapClient;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityElytraSpeedMixin {

    @Inject(method = "updateFallFlyingMovement", at = @At(value = "RETURN", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), cancellable = true)
    private void elytraspeedcap$capFallFlyingVelocity(Vec3 input, CallbackInfoReturnable<Vec3> cir) {
        // Get the original velocity vector
        Vec3 velocity = cir.getReturnValue();

        // Cap the velocity vector, returning early if no cap is needed
        Vec3 capped = ElytraSpeedCap.capElytraVelocity(ElytraSpeedCapClient.maxSpeed, velocity);
        if (capped == null)
            return;

        // Send capped vector back
        cir.setReturnValue(capped);
    }
}
