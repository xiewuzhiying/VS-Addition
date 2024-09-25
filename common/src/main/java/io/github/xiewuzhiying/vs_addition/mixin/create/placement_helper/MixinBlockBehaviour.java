package io.github.xiewuzhiying.vs_addition.mixin.create.placement_helper;

import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import io.github.xiewuzhiying.vs_addition.compats.create.placement_helper.TestFlapBlockPlacementHelper;
import io.github.xiewuzhiying.vs_addition.compats.create.placement_helper.TestWingBlockPlacementHelper;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.block.TestFlapBlock;
import org.valkyrienskies.mod.common.block.TestWingBlock;

@Pseudo
@Restriction(
        require = @Condition("create")
)
@Mixin(BlockBehaviour.class)
public abstract class MixinBlockBehaviour {
    @Unique
    private static final int vs_addition$wingPlacementHelperId = PlacementHelpers.register(new TestWingBlockPlacementHelper());
    @Unique
    private static final int vs_addition$flapPlacementHelperId = PlacementHelpers.register(new TestFlapBlockPlacementHelper());

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if ((BlockBehaviour)(Object)this instanceof TestFlapBlock || (BlockBehaviour)(Object)this instanceof TestWingBlock) {
            IPlacementHelper placementHelper = null;
            if(((BlockBehaviour)(Object)this) instanceof TestWingBlock) {
                placementHelper = PlacementHelpers.get(vs_addition$wingPlacementHelperId);
            } else if(((BlockBehaviour)(Object)this) instanceof TestFlapBlock) {
                placementHelper = PlacementHelpers.get(vs_addition$flapPlacementHelperId);
            }
            if (placementHelper != null && !player.isShiftKeyDown() && player.mayBuild()) {
                ItemStack heldItem = player.getItemInHand(hand);
                if (placementHelper.matchesItem(heldItem)) {
                    placementHelper.getOffset(player, level, state, pos, hit)
                            .placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hit);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }
}
