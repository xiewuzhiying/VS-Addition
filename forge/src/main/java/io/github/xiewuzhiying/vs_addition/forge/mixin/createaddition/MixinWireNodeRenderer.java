package io.github.xiewuzhiying.vs_addition.forge.mixin.createaddition;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mrh0.createaddition.energy.IWireNode;
import com.mrh0.createaddition.rendering.WireNodeRenderer;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WireNodeRenderer.class)
public abstract class MixinWireNodeRenderer {
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    ordinal = 0
            )
    )
    private void man(Args args, @Local(ordinal = 0) IWireNode te, @Local(ordinal = 0) BlockPos other, @Local(ordinal = 0) Vec3 d1, @Local(ordinal = 1) Vec3 d2, @Share("tePos") LocalRef<Vec3> tePos, @Share("otherPos") LocalRef<Vec3> otherPos) {
        tePos.set(VSGameUtilsKt.toWorldCoordinates(Minecraft.getInstance().level, te.getPos().getCenter().add(d1)));
        otherPos.set(VSGameUtilsKt.toWorldCoordinates(Minecraft.getInstance().level, other.getCenter().add(d2)));

        final Vec3 diff = otherPos.get().subtract(TransformUtils.toVec3(te.getPos()));
        args.set(0, (float)diff.x);
        args.set(1, (float)diff.y);
        args.set(2, (float)diff.z);
    }

    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;wireRender(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FFFLcom/mrh0/createaddition/energy/WireType;F)V",
                    ordinal = 0
            ),
            remap = false
    )
    private void man2(Args args, @Share("tePos") LocalRef<Vec3> tePos, @Share("otherPos") LocalRef<Vec3> otherPos) {
        final Vec3 diff = otherPos.get().subtract(tePos.get());
        args.set(4, (float)diff.x);
        args.set(5, (float)diff.y);
        args.set(6, (float)diff.z);
    }
}
