package io.github.xiewuzhiying.vs_addition.fabric.mixin.voxelmap.client;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mamiyaotaru.voxelmap.util.GameVariableAccessShim;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(GameVariableAccessShim.class)
public abstract class MixinGameVariableAccessShim {
    @Unique
    private static Matrix4dc vs_addition$latestMatrix4d = new Matrix4d();

    @Unique
    private static float vs_addition$shipYawAngle = 0;

    @Inject(
            method = "rotationYaw",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void useActualAngle(CallbackInfoReturnable<Float> cir, @Share("cache") LocalFloatRef cache, @Share("isCached") LocalBooleanRef isCached) {
        if (isCached.get()) {
            cir.setReturnValue(cache.get());
            return;
        }
        ClientShip ship = vs_addition$getMountedShip();
        if (ship != null) {
            vs_addition$updateShipData(ship);
            cache.set(cir.getReturnValue() + vs_addition$shipYawAngle);
            isCached.set(true);
            cir.setReturnValue(cache.get());
        }
        vs_addition$resetShipData();
        cache.set(cir.getReturnValue() + vs_addition$shipYawAngle);
        isCached.set(true);
        cir.setReturnValue(cache.get());
    }

    @Unique
    private static ClientShip vs_addition$getMountedShip() {
        return (ClientShip) VSGameUtilsKt.getShipMountedTo(Minecraft.getInstance().gameRenderer.getMainCamera().getEntity());
    }

    @Unique
    private static void vs_addition$updateShipData(ClientShip ship) {
        Matrix4d matrix = ship.getRenderTransform()
                .getShipToWorld()
                .invert(new Matrix4d())
                .mul(vs_addition$latestMatrix4d);

        vs_addition$latestMatrix4d = ship.getRenderTransform().getShipToWorld();
        vs_addition$shipYawAngle += Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
    }

    @Unique
    private static void vs_addition$resetShipData() {
        vs_addition$latestMatrix4d = new Matrix4d();
        vs_addition$shipYawAngle = 0;
    }
}
