package io.github.nwrenger.elytraspeedcap.mixin.client;

import io.github.nwrenger.elytraspeedcap.ElytraSpeedCap;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapClient;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityElytraSpeedMixin {

    @Inject(method = "updateFallFlyingMovement", at = @At("RETURN"), cancellable = true)
    private void elytraspeedcap$capFallFlyingVelocity(Vec3 input,
            CallbackInfoReturnable<Vec3> cir) {
        Vec3 result = cir.getReturnValue();

        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof Player) || !self.isFallFlying()) {
            return;
        }

        double maxSpeedPerTick = ElytraSpeedCapConfig.intoMaxSpeedPerTick(ElytraSpeedCapClient.maxSpeed);
        if (maxSpeedPerTick <= 0.0D) {
            return;
        }

        double horizontal = Math.sqrt(result.x * result.x + result.z * result.z);
        if (horizontal <= maxSpeedPerTick) {
            return;
        }

        double factor = maxSpeedPerTick / horizontal;
        Vec3 capped = result.scale(factor);

        ElytraSpeedCap.LOGGER.debug(
                "[Elytra Speed Cap] Capping HORIZONTAL Elytra velocity for {} from {} to {} (factor={}) oldVel={} newVel={}",
                self.getName().getString(),
                horizontal,
                maxSpeedPerTick,
                factor,
                result,
                capped);

        // Send capped vector back
        cir.setReturnValue(capped);
    }
}
