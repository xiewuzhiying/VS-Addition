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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.physicalities.wing.FlapBlock;
import org.valkyrienskies.clockwork.content.physicalities.wing.WingBlock;
import org.valkyrienskies.clockwork.util.blocktype.DyedWing;

@Pseudo
@Mixin(DyedWing.class)
public abstract class MixinDyedWing {
    @Unique
    private static final int vs_addition$wingPlacementHelperId = PlacementHelpers.register(new WingPlacementHelper());
    @Unique
    private static final int vs_addition$flapPlacementHelperId = PlacementHelpers.register(new FlapPlacementHelper());
    
    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        final IPlacementHelper placementHelper;
        if((DyedWing)(Object)this instanceof WingBlock) {
            placementHelper = PlacementHelpers.get(vs_addition$wingPlacementHelperId);
        } else if((DyedWing)(Object)this instanceof FlapBlock) {
            placementHelper = PlacementHelpers.get(vs_addition$flapPlacementHelperId);
        } else {
            return;
        }
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, level, state, pos, hit)
                        .placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hit);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
