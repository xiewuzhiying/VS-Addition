package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.FlapBearing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.redstone.link.LinkRenderer;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.Link.SecondLinkRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingRenderer;

@Mixin(FlapBearingRenderer.class)
public abstract class MixinFlapBearingRenderer extends KineticBlockEntityRenderer<FlapBearingBlockEntity> {
    public MixinFlapBearingRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
            method = "renderSafe(Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("RETURN")
    )
    private void renderLinkSlots(FlapBearingBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        LinkRenderer.renderOnBlockEntity(te, partialTicks, ms, buffer, light, overlay);
        SecondLinkRenderer.renderOnBlockEntity(te, partialTicks, ms, buffer, light, overlay);
    }
}
