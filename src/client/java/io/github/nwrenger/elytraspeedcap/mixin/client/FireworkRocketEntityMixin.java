package io.github.nwrenger.elytraspeedcap.mixin.client;

import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.github.nwrenger.elytraspeedcap.ElytraSpeedCap;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapClient;
import io.github.nwrenger.elytraspeedcap.ElytraSpeedCapConfig;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), index = 0)
    private Vec3 elytraSpeedCap$modifyRocketBoost(Vec3 vanillaBoost) {
        double maxSpeedPerTick = ElytraSpeedCapConfig.intoMaxSpeedPerTick(ElytraSpeedCapClient.maxSpeed);
        if (maxSpeedPerTick <= 0.0D) {
            return vanillaBoost;
        }

        double horizontal = Math.sqrt(vanillaBoost.x * vanillaBoost.x + vanillaBoost.z * vanillaBoost.z);
        if (horizontal <= maxSpeedPerTick) {
            return vanillaBoost;
        }

        double factor = maxSpeedPerTick / horizontal;
        Vec3 capped = vanillaBoost.scale(factor);

        ElytraSpeedCap.LOGGER.debug(
                "[Elytra Speed Cap] Capping FIREWORK Elytra boost from {} to {} (factor={}) oldBoost={} newBoost={}",
                horizontal,
                maxSpeedPerTick,
                factor,
                vanillaBoost,
                capped);

        vanillaBoost = capped;
        return vanillaBoost;
    }
}
