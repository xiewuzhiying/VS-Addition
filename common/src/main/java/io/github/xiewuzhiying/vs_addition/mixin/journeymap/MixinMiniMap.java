package io.github.xiewuzhiying.vs_addition.mixin.journeymap;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import journeymap.client.ui.minimap.MiniMap;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(MiniMap.class)
public abstract class MixinMiniMap {
    @Shadow @Final private Minecraft mc;

    @Unique
    private Matrix4dc latestMatrix4d = new Matrix4d();

    @Unique
    private float shipYawAngle = 0;

    @Unique
    private Ship ship = null;

    @ModifyExpressionValue(
            method = "drawMap(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"
            )
    )
    private float useActualAngle(float original) {
        this.ship = VSGameUtilsKt.getShipMountedTo(mc.player);
        if (this.ship != null) {
            Matrix4d matrix = this.ship.getTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
            latestMatrix4d = this.ship.getTransform().getShipToWorld();
            shipYawAngle = (float) (shipYawAngle - Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z)) / 2);
            return original + shipYawAngle;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "drawMap(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 1
            )
    )
    private float useCachedAngle1(float original) {
        if(ship!=null)
            return original + this.shipYawAngle;
        else
            return original;
    }

    @ModifyExpressionValue(
            method = "drawMap(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 2
            )
    )
    private float useCachedAngle2(float original) {
        if(ship!=null)
            return original + this.shipYawAngle;
        else
            return original;
    }

    @ModifyExpressionValue(
            method = "drawMap(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 3
            )
    )
    private float useCachedAngle3(float original) {
        if(ship!=null)
            return original + this.shipYawAngle;
        else
            return original;
    }

    @ModifyExpressionValue(
            method = "drawMap(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
                    ordinal = 4
            )
    )
    private float useCachedAngle4(float original) {
        if(ship!=null)
            return original + this.shipYawAngle;
        else
            return original;
    }
}
