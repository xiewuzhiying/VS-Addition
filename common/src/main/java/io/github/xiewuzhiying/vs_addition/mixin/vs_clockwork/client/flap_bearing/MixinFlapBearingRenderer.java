package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.client.flap_bearing;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.redstone.link.LinkRenderer;
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingRenderer;

@Pseudo
@Mixin(FlapBearingRenderer.class)
public abstract class MixinFlapBearingRenderer {
    @Inject(
            method = "renderSafe(Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("RETURN")
    )
    private void renderLinkSlots(FlapBearingBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                 int light, int overlay, CallbackInfo ci) {
        LinkRenderer.renderOnBlockEntity(blockEntity, partialTicks, ms, buffer, light, overlay);
        DualLinkRenderer.renderOnBlockEntity(blockEntity, partialTicks, ms, buffer, light, overlay);
    }

}
