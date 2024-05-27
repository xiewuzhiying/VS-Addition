package io.github.xiewuzhiying.vs_addition.mixin.xaeros_minimap;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xaero.common.minimap.render.MinimapRenderer;

@Mixin(MinimapRenderer.class)
public interface MinimapRendererAccessor {
    @Accessor("mc")
    Minecraft getMinecraft();
}
