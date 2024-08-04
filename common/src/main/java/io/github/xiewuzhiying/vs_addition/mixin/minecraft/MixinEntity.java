package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract float getYRot();

    @Shadow public abstract void setYRot(float yRot);

    @Shadow public Level level;

    @Inject(
            method = "removeVehicle",
            at = @At("HEAD")
    )
    private void addShipYaw(CallbackInfo ci) {
        Ship ship = VSGameUtilsKt.getShipMountedTo((Entity)(Object)this);
        if (ship == null)
            return;

        Matrix4d matrix = (Matrix4d) ship.getTransform().getShipToWorld();
        double yaw = Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
        this.setYRot((float) (this.getYRot() + yaw) % 360);
    }


    //    Who called this method twice?????
    @Inject(
            method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
            at = @At("RETURN")
    )
    private void subShipYaw(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if(!level.isClientSide || !cir.getReturnValue())
            return;

        Ship ship = VSGameUtilsKt.getShipMountedTo((Entity)(Object)this);
        if (ship == null)
            return;

        Matrix4d matrix = (Matrix4d) ship.getTransform().getShipToWorld();
        double yaw = Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
        this.setYRot((float) (this.getYRot() - yaw) % 360);
    }

}
