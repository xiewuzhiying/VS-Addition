package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork;

import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.FlapPlacementHelper;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.WingPlacementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.physicalities.wing.FlapBlock;
import org.valkyrienskies.clockwork.content.physicalities.wing.WingBlock;
import org.valkyrienskies.clockwork.util.blocktype.DyedWing;

@Mixin(DyedWing.class)
public abstract class MixinDyedWing {
    private static final int wingPlacementHelperId = PlacementHelpers.register(new WingPlacementHelper());
    private static final int flapPlacementHelperId = PlacementHelpers.register(new FlapPlacementHelper());
    
    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack heldItem = player.getItemInHand(hand);

        IPlacementHelper placementHelper = null;
        if(((DyedWing)(Object)this) instanceof WingBlock) {
            placementHelper = PlacementHelpers.get(wingPlacementHelperId);
        } else if(((DyedWing)(Object)this) instanceof FlapBlock) {
            placementHelper = PlacementHelpers.get(flapPlacementHelperId);
        }
        if (placementHelper != null && !player.isShiftKeyDown() && player.mayBuild()) {
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, level, state, pos, hit)
                        .placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hit);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
