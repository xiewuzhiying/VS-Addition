package io.github.xiewuzhiying.vs_addition.mixin.xaeros_minimap.client;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import xaero.common.minimap.render.MinimapRenderer;

@Mixin(MinimapRenderer.class)
public abstract class MixinMinimapRenderer {

    @Shadow protected Minecraft mc;
    @Unique
    private Matrix4dc latestMatrix4d = new Matrix4d();

    @Unique
    private double shipYawAngle = 0;

    @ModifyExpressionValue(
            method = "getRenderAngle",
            at = @At(
                    value = "INVOKE",
                    target = "Lxaero/common/minimap/render/MinimapRenderer;getActualAngle()D"
            ),
            remap = false
    )
    public double useActualAngle(double original) {
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipMountedTo(mc.gameRenderer.getMainCamera().getEntity());
        if (ship != null) {
            Matrix4d matrix = ship.getRenderTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
            latestMatrix4d = ship.getRenderTransform().getShipToWorld();
            shipYawAngle = shipYawAngle + Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
            return original + shipYawAngle;
        } else {
            return original;
        }
    }
}
