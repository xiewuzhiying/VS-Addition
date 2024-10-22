package io.github.xiewuzhiying.vs_addition.forge.mixin.tacz.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.tacz.guns.client.particle.BulletHoleParticle;
import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ParticleMixinDuck;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ClientShip;

@Pseudo
@Mixin(BulletHoleParticle.class)
public abstract class MixinBulletHoleParticle extends TextureSheetParticle {

    protected MixinBulletHoleParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/TextureSheetParticle;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updatePosition(CallbackInfo ci) {
        final ClientShip ship = ((ParticleMixinDuck)this).vs_addition$getShip();
        if (ship != null) {
            final Vector3d worldPos = ship.getRenderTransform().getShipToWorld().transformPosition(new Vector3d(((ParticleMixinDuck)this).vs_addition$getOriginalPosition()));
            this.setPos(worldPos.x, worldPos.y, worldPos.z);
        }
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(DDD)D",
                    ordinal = 0
            )
    )
    private double inclShipMovement1(double original, @Share("modified") LocalBooleanRef modified, @Share("y") LocalDoubleRef y, @Share("z") LocalDoubleRef z) {
        final ClientShip ship = ((ParticleMixinDuck)this).vs_addition$getShip();
        if (ship != null) {
            modified.set(true);
            final Vector3d worldPos = ship.getRenderTransform().getShipToWorld().transformPosition(new Vector3d(((ParticleMixinDuck)this).vs_addition$getOriginalPosition()));
            y.set(worldPos.y);
            z.set(worldPos.z);
            this.setPos(worldPos.x, worldPos.y, worldPos.z);
            return worldPos.x;
        } else {
            modified.set(false);
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(DDD)D",
                    ordinal = 1
            )
    )
    private double inclShipMovement2(double original, @Share("modified") LocalBooleanRef modified, @Share("y") LocalDoubleRef y) {
        if (modified.get()) {
            return y.get();
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(DDD)D",
                    ordinal = 2
            )
    )
    private double inclShipMovement3(double original, @Share("modified") LocalBooleanRef modified, @Share("z") LocalDoubleRef z) {
        if (modified.get()) {
            return z.get();
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/Direction;getRotation()Lorg/joml/Quaternionf;"
            )
    )
    private Quaternionf inclShipRotate(final Quaternionf original, @Share("modified") LocalBooleanRef modified) {
        if (modified.get()) {
            final ClientShip ship = ((ParticleMixinDuck)this).vs_addition$getShip();
            final Quaternionf shipRot = ship.getRenderTransform().getShipToWorldRotation().get(new Quaternionf());
            return shipRot.mul(original);
        } else {
            return original;
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;",
                    remap = false
            )
    )
    private Vector3f inclShipScale(Vector3f instance, float scalar, Operation<Vector3f> original, @Share("modified") LocalBooleanRef modified) {
        if (modified.get()) {
            return original.call(instance, (float) (scalar * (((ParticleMixinDuck)this).vs_addition$getShip().getRenderTransform().getShipToWorld().getScale(new Vector3d()).z / ((ParticleMixinDuck)this).vs_addition$getFirstTimeScale())));
        } else {
            return original.call(instance, scalar);
        }
    }
}
