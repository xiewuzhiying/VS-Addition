package io.github.xiewuzhiying.vs_addition.mixin.create.display_link;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.VSGameUtilsKt;


@Mixin(DisplayLinkBlockItem.class)
public abstract class MixinDisplayLinkBlockItem extends BlockItem {

    public MixinDisplayLinkBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Unique
    private Level vs_addition$accessedLevel;

    @Inject(
            method = "useOn",
            at = @At("HEAD")
    )
    public void vs_addition$getLevel(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        vs_addition$accessedLevel = pContext.getLevel();
    }

    @Redirect(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            )
    )
    public boolean closerThan(BlockPos instance, Vec3i pVector, double pDistance) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(vs_addition$accessedLevel, instance.getX(), instance.getY(), instance.getZ(), pVector.getX(), pVector.getY(), pVector.getZ()) < Mth.square(pDistance);
    }
}
