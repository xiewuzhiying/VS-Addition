package io.github.xiewuzhiying.vs_addition.mixin.create.Copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import java.util.List;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.apigame.world.ShipWorldCore;
import org.valkyrienskies.core.apigame.world.chunks.BlockType;
import org.valkyrienskies.mod.common.BlockStateInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.DimensionIdProvider;

@Mixin(CopycatBlock.class)
public abstract class MixinCopycatBlock extends Block implements IBE<CopycatBlockEntity>, IWrenchable {

    @Unique
    private static double vs_addition$calculateTotalVolume(final List<AABB> aabbList) {
        double totalVolume = 0.0;

        for (final AABB aabb : aabbList) {
            final double width = aabb.maxX - aabb.minX;
            final double height = aabb.maxY - aabb.minY;
            final double depth = aabb.maxZ - aabb.minZ;
            totalVolume += width * height * depth;
        }

        return totalVolume;
    }

    public MixinCopycatBlock(final Properties pProperties) {
        super(pProperties);
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;removeBlockEntity(Lnet/minecraft/core/BlockPos;)V",
                    shift = Shift.BEFORE
            ),
            method = "onRemove"
    )
    public void onRemove(final BlockState pState, final Level pLevel, final BlockPos pPos, final BlockState pNewState, final boolean pIsMoving,
                         final CallbackInfo ci) {


        final ShipWorldCore shipObjectWorld = VSGameUtilsKt.getShipObjectWorld(pLevel);

        final Double multiplier = vs_addition$calculateTotalVolume(pState.getShape(pLevel, pPos).toAabbs());

        final Double Oldmass;

        final Pair<Double, BlockType> State = BlockStateInfo.INSTANCE.get(pState);

        final Pair<Double, BlockType> NewState = BlockStateInfo.INSTANCE.get(pNewState);

        if(getBlockEntity(pLevel, pPos).getMaterial()== AllBlocks.COPYCAT_BASE.getDefaultState())
            Oldmass = BlockStateInfo.INSTANCE.get(Blocks.AIR.defaultBlockState()).getFirst();
        else
            Oldmass = BlockStateInfo.INSTANCE.get(getBlockEntity(pLevel, pPos).getMaterial()).getFirst() * multiplier;

        shipObjectWorld.onSetBlock(pPos.getX(), pPos.getY(), pPos.getZ(), ((DimensionIdProvider) pLevel).getDimensionId(), State.getSecond(), NewState.getSecond(), Oldmass, NewState.getFirst());
    }
}
