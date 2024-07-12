package io.github.xiewuzhiying.vs_addition.mixin.create.display_link;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(DisplayLinkBlockEntity.class)
public abstract class MixinDisplayLinkBlockEntity extends SmartBlockEntity {
    public MixinDisplayLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ModifyExpressionValue(
            method = "updateGatheredData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isLoaded(Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 1
            )
    )
    public boolean distanceLimit(boolean original, @Local(ordinal = 0) BlockPos sourcePosition, @Local(ordinal = 1) BlockPos targetPosition) {
        return original || VSGameUtilsKt.squaredDistanceBetweenInclShips(this.level, sourcePosition.getX(), sourcePosition.getY(), sourcePosition.getZ(), targetPosition.getX(), targetPosition.getY(), targetPosition.getZ()) > Mth.square(AllConfigs.server().logistics.displayLinkRange.get());
    }
}
