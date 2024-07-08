package io.github.xiewuzhiying.vs_addition.fabric.mixin.voxelmap;

import com.mamiyaotaru.voxelmap.util.GameVariableAccessShim;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(GameVariableAccessShim.class)
public abstract class MixinGameVariableAccessShim {
    @Shadow private static Minecraft minecraft;
    @Unique
    private static Matrix4dc latestMatrix4d = new Matrix4d();

    @Unique
    private static float shipYawAngle = 0;
    @Inject(
            method = "rotationYaw",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void useActualAngle(CallbackInfoReturnable<Float> cir) {
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipMountedTo(minecraft.gameRenderer.getMainCamera().getEntity());
        if (ship != null) {
            Matrix4d matrix = ship.getRenderTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
            latestMatrix4d = ship.getRenderTransform().getShipToWorld();
            shipYawAngle = (float) (shipYawAngle + Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z)));
            cir.setReturnValue(cir.getReturnValue() - shipYawAngle);
        }
    }
}
