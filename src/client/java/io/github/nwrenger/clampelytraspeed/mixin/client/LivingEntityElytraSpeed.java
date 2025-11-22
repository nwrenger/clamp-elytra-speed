package io.github.nwrenger.clampelytraspeed.mixin.client;

import io.github.nwrenger.clampelytraspeed.ClampElytraSpeed;
import io.github.nwrenger.clampelytraspeed.ClampElytraSpeedClient;
import io.github.nwrenger.clampelytraspeed.ClampElytraSpeedConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityElytraSpeed extends Entity {
    protected LivingEntityElytraSpeed(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "updateFallFlyingMovement", at = @At("RETURN"), cancellable = true)
    private void clampelytraspeed$clampFallFlyingVelocity(Vec3 input,
            CallbackInfoReturnable<Vec3> cir) {
        Vec3 result = cir.getReturnValue();

        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof Player) || !self.isFallFlying()) {
            return;
        }

        double maxSpeedPerTick = ClampElytraSpeedConfig.intoMaxSpeedPerTick(ClampElytraSpeedClient.maxSpeed);
        if (maxSpeedPerTick <= 0.0D) {
            return;
        }

        double horizontal = Math.sqrt(result.x * result.x + result.z * result.z);
        if (horizontal <= maxSpeedPerTick) {
            return;
        }

        double factor = maxSpeedPerTick / horizontal;
        Vec3 clamped = new Vec3(
                result.x * factor,
                result.y,
                result.z * factor);

        ClampElytraSpeed.LOGGER.debug(
                "[Clamp Elytra Speed] Clamping HORIZONTAL Elytra velocity for {} from {} to {} (factor={}) oldVel={} newVel={}",
                self.getName().getString(),
                horizontal,
                maxSpeedPerTick,
                factor,
                result,
                clamped);

        // Send clamped vector back
        cir.setReturnValue(clamped);
    }
}
