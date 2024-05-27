package io.github.xiewuzhiying.vs_addition.mixin.xaeros_minimap;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import xaero.common.minimap.render.MinimapRenderer;

@Mixin(MinimapRenderer.class)
public abstract class MixinMinimapRenderer {

    @WrapOperation(
            method = "getRenderAngle",
            at = @At(
                    value = "INVOKE",
                    target = "Lxaero/common/minimap/render/MinimapRenderer;getActualAngle()D"
            ),
            remap = false
    )
    public double getActualAngle(MinimapRenderer instance, Operation<Double> original) {
        Minecraft mc = ((MinimapRendererAccessor) instance).getMinecraft();
        Camera camera = mc.gameRenderer.getMainCamera();
        Player player = (Player) camera.getEntity();
        Entity vehicle = player.getVehicle();

        if(vehicle != null){
            ClientShip ship = VSClientGameUtils.getClientShip(vehicle.position().x, vehicle.position().y, vehicle.position().z);
            if (ship != null) {
                Vector3d vec = new Vector3d(0, 0, 1);
                Vector3d shipRot = new Vector3d(vec).rotate(ship.getRenderTransform().getShipToWorldRotation());
                shipRot.y = 0;
                double shipRotAngle = Math.toDegrees(Math.acos(vec.dot(shipRot)) * Math.signum(vec.cross(shipRot).y));
                return original.call(instance) + shipRotAngle;
            } else {
                return original.call(instance);
            }
        } else {
            return original.call(instance);
        }
    }
}
