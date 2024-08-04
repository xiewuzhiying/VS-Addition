package io.github.xiewuzhiying.vs_addition.fabric.mixin.sodium.client;

import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(DefaultChunkRenderer.class)
public class MixinDefaultChunkRenderer {
    @Inject(
            method = "getVisibleFaces",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void cancelBlockFaceCulling(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, CallbackInfoReturnable<Integer> cir) {
        if(VSGameUtilsKt.isChunkInShipyard(Minecraft.getInstance().level, chunkX, chunkZ))
            cir.setReturnValue(ModelQuadFacing.ALL);
    }
}
