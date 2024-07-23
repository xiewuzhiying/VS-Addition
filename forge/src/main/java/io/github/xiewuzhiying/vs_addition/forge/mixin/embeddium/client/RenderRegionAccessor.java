package io.github.xiewuzhiying.vs_addition.forge.mixin.embeddium.client;

import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RenderRegion.class, remap = false)
public interface RenderRegionAccessor {
    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor("z")
    int getZ();
}
