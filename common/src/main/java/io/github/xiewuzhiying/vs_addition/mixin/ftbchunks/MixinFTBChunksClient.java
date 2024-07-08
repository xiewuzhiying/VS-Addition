package io.github.xiewuzhiying.vs_addition.mixin.ftbchunks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(FTBChunksClient.class)
public abstract  class MixinFTBChunksClient {
    @Unique
    private Matrix4dc latestMatrix4d = new Matrix4d();

    @Unique
    private float shipYawAngle = 0;
    @ModifyExpressionValue(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 0
            )
    )
    public float useActualAngle1(float original) {
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipMountedTo(Minecraft.getInstance().gameRenderer.getMainCamera().getEntity());
        if (ship != null) {
            Matrix4d matrix = ship.getRenderTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
            latestMatrix4d = ship.getRenderTransform().getShipToWorld();
            shipYawAngle = (float) (shipYawAngle + Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z)));
            return original - shipYawAngle;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 1
            )
    )
    public float useActualAngle2(float original) {
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipMountedTo(Minecraft.getInstance().gameRenderer.getMainCamera().getEntity());
        if (ship != null) {
            Matrix4d matrix = ship.getRenderTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
            latestMatrix4d = ship.getRenderTransform().getShipToWorld();
            shipYawAngle = (float) (shipYawAngle + Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z)));
            return original + shipYawAngle;
        } else {
            return original;
        }
    }
}
