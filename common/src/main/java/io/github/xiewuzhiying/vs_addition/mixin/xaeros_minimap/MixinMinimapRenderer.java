package io.github.xiewuzhiying.vs_addition.mixin.xaeros_minimap;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import xaero.common.minimap.render.MinimapRenderer;

@Mixin(MinimapRenderer.class)
public abstract class MixinMinimapRenderer {

    @Unique
    private Matrix4dc latestMatrix4d = new Matrix4d();

    @Unique
    private double shipYawAngle = 0;

    @WrapOperation(
            method = "getRenderAngle",
            at = @At(
                    value = "INVOKE",
                    target = "Lxaero/common/minimap/render/MinimapRenderer;getActualAngle()D"
            ),
            remap = false
    )
    public double getActualAngle(MinimapRenderer instance, Operation<Double> original) {
        Entity vehicle = ((MinimapRendererAccessor) instance).getMinecraft().gameRenderer.getMainCamera().getEntity().getVehicle();

        if(vehicle != null){
            ClientShip ship = VSClientGameUtils.getClientShip(vehicle.position().x, vehicle.position().y, vehicle.position().z);
            if (ship != null) {
                Matrix4d matrix = ship.getRenderTransform().getShipToWorld().invert(new Matrix4d()).mul(latestMatrix4d);
                latestMatrix4d = ship.getRenderTransform().getShipToWorld();
                shipYawAngle = shipYawAngle + Math.toDegrees(Math.atan2(-matrix.getRow(0, new Vector3d()).z, matrix.getRow(2, new Vector3d()).z));
                return original.call(instance) + shipYawAngle;
            } else {
                return original.call(instance);
            }
        } else {
            return original.call(instance);
        }
    }
}
