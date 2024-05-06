package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

import java.util.List;

@Mixin(CopycatBlockEntity.class)
public abstract class MixinCopycatBlockEntity extends SmartBlockEntity{

    @Shadow
    private BlockState material;
    @Shadow
    private ItemStack consumedItem;

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


    public MixinCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        material = AllBlocks.COPYCAT_BASE.getDefaultState();
        consumedItem = ItemStack.EMPTY;
    }

    @Inject(
            method = "setMaterial",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlockEntity;getBlockState()Lnet/minecraft/world/level/block/state/BlockState;",
                    shift = Shift.AFTER
            )
    )
    public void setMaterial(final BlockState blockState, final CallbackInfo ci) {

        final ShipWorldCore shipObjectWorld = VSGameUtilsKt.getShipObjectWorld(level);

        final Double multiplier = vs_addition$calculateTotalVolume(getBlockState().getShape(level, worldPosition).toAabbs());

        final Pair<Double, BlockType> State = BlockStateInfo.INSTANCE.get(getBlockState());

        final Double Oldmass;

        final Double Newmass;

        if(material==AllBlocks.COPYCAT_BASE.getDefaultState())
            Oldmass = BlockStateInfo.INSTANCE.get(Blocks.AIR.defaultBlockState()).getFirst();
        else
            Oldmass = BlockStateInfo.INSTANCE.get(material).getFirst() * multiplier;


        if(blockState==AllBlocks.COPYCAT_BASE.getDefaultState())
            Newmass = BlockStateInfo.INSTANCE.get(Blocks.AIR.defaultBlockState()).getFirst();
        else
            Newmass = BlockStateInfo.INSTANCE.get(blockState).getFirst() * multiplier;

        shipObjectWorld.onSetBlock(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ((DimensionIdProvider) level).getDimensionId(), State.getSecond(), State.getSecond(), Oldmass, Newmass);
    }

}
