package io.github.xiewuzhiying.vs_addition.mixin.xaeros_minimap.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import xaero.common.minimap.render.MinimapRenderer;

@Pseudo
@Mixin(MinimapRenderer.class)
public abstract class MixinMinimapRenderer {

    @Shadow protected Minecraft mc;
    @Unique
    private Matrix4dc vs_addition$latestMatrix4d = new Matrix4d();
    @Unique
    private double vs_addition$shipYawAngle = 0;

    @WrapOperation(
            method = "renderMinimap",
            at = @At(
                    value = "INVOKE",
                    target = "Lxaero/common/minimap/render/MinimapRenderer;getRenderAngle(Z)D"
            ),
            remap = false
    )
    public double modifyAngle1(MinimapRenderer instance, boolean lockedNorth, Operation<Double> original) {
        if (lockedNorth) {
            vs_addition$resetShipData();
            return original.call(instance, true);
        } else {
            ClientShip ship = vs_addition$getMountedShip();
            if (ship != null) {
                vs_addition$updateShipData(ship);
                return original.call(instance, false) + vs_addition$shipYawAngle;
            }
            vs_addition$resetShipData();
            return original.call(instance, false);
        }
    }

    @ModifyExpressionValue(
            method = "renderMinimap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"
            )
    )
    private float modifyAngle2(float original) {
        ClientShip ship = vs_addition$getMountedShip();
        if (ship != null) {
            vs_addition$updateShipData(ship);
            return (float) (original - vs_addition$shipYawAngle);
        }
        vs_addition$resetShipData();
        return original;
    }

    @Unique
    private ClientShip vs_addition$getMountedShip() {
        return (ClientShip) VSGameUtilsKt.getShipMountedTo(mc.gameRenderer.getMainCamera().getEntity());
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