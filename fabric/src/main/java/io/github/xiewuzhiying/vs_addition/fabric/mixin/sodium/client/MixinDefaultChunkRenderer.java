package io.github.xiewuzhiying.vs_addition.fabric.mixin.sodium.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;

@Mixin(DefaultChunkRenderer.class)
public class MixinDefaultChunkRenderer {
    @WrapOperation(
            method = "fillCommandBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/DefaultChunkRenderer;getVisibleFaces(IIIIII)I"
            ),
            remap = false
    )
    private static int transformToShipCoordinates(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, Operation<Integer> original,
                           @Local(argsOnly = true) RenderRegion renderRegion, @Local(argsOnly = true) CameraTransform camera) {
        ClientShip clientShip = VSClientGameUtils.getClientShip(((RenderRegionAccessor)renderRegion).getX(), ((RenderRegionAccessor)renderRegion).getY(), ((RenderRegionAccessor)renderRegion).getZ());
        if(clientShip != null){
            Vector3d vec = clientShip.getRenderTransform().transformDirectionNoScalingFromWorldToShip(new Vector3d(camera.x, camera.y, camera.z), new Vector3d());
            CameraTransform newCamera = new CameraTransform(vec.x, vec.y, vec.z);
            return original.call(newCamera.intX, newCamera.intY, newCamera.intZ, chunkX, chunkY, chunkZ);
        }
        return original.call(originX, originY, originZ, chunkX, chunkY, chunkZ);
    }
}
