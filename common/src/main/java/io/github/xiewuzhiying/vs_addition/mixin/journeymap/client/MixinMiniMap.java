package io.github.xiewuzhiying.vs_addition.mixin.journeymap.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import journeymap.client.ui.minimap.MiniMap;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(MiniMap.class)
public abstract class MixinMiniMap {
    @Shadow @Final private Minecraft mc;

    @Unique
    private Matrix4dc vs_addition$latestMatrix4d = new Matrix4d();

    @Unique
    private float vs_addition$shipYawAngle = 0;

    @ModifyExpressionValue(
            method = "drawMap(Lnet/minecraft/client/gui/GuiGraphics;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"
            )
    )
    private float useActualAngle(float original, @Share("cache") LocalFloatRef cache, @Share("isCached") LocalBooleanRef isCached) {
        if (isCached.get()) {
            return cache.get();
        }
        ClientShip ship = vs_addition$getMountedShip();
        if (ship != null) {
            vs_addition$updateShipData(ship);
            cache.set(original + vs_addition$shipYawAngle);
            isCached.set(true);
            return cache.get();
        }
        vs_addition$resetShipData();
        cache.set(original + vs_addition$shipYawAngle);
        isCached.set(true);
        return cache.get();
    }

    @Unique
    private ClientShip vs_addition$getMountedShip() {
        return (ClientShip) VSGameUtilsKt.getShipMountedTo(Minecraft.getInstance().gameRenderer.getMainCamera().getEntity());
    }

    @Unique
    private void vs_addition$updateShipData(ClientShip ship) {
        Matrix4d matrix = ship.getRenderTransform()
                .getShipToWorld()
                .invert(new Matrix4d())
                .mul(vs_addition$latestMatrix4d);

        vs_addition$latestMatrix4d = ship.getRenderTransform().getShipToWorld();
        vs_addition$shipYawAngle += Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
    }

    @Unique
    private void vs_addition$resetShipData() {
        vs_addition$latestMatrix4d = new Matrix4d();
        vs_addition$shipYawAngle = 0;
    }
}
