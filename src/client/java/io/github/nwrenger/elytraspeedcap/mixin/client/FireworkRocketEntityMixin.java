package io.github.nwrenger.elytraspeedcap.mixin.client;

import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.github.nwrenger.elytraspeedcap.ElytraSpeedCap;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapClient;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), index = 0)
    private Vec3 elytraSpeedCap$modifyRocketBoost(Vec3 vanillaBoost) {
        // Cap the velocity vector, returning early if no cap is needed
        Vec3 capped = ElytraSpeedCap.capElytraVelocity(ElytraSpeedCapClient.maxSpeed, vanillaBoost);
        if (capped == null)
            return vanillaBoost;

        // Return the capped velocity vector
        return capped;
    }
}
