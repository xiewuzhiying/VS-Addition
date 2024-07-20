package io.github.xiewuzhiying.vs_addition.fabric.mixin.sodium.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.chunk.RegionChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(RegionChunkRenderer.class)
public class MixinDefaultChunkRenderer {
    @ModifyExpressionValue(
            method = "buildDrawBatches",
            at = @At(
                    value = "FIELD",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/RegionChunkRenderer;isBlockFaceCullingEnabled:Z"
            ),
            remap = false
    )
    private boolean cancelBlockFaceCulling(boolean original, @Local ChunkRenderBounds bounds) {
        if(VSGameUtilsKt.isBlockInShipyard(Minecraft.getInstance().level, bounds.x1, bounds.y1, bounds.z1))
            return false;
        return original;
    }
}
