//package io.github.xiewuzhiying.vs_addition.mixin.copycats;
//
//
//import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
//import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
//import com.simibubi.create.AllBlocks;
//import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
//import kotlin.Pair;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import org.jetbrains.annotations.NotNull;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.valkyrienskies.core.apigame.world.ShipWorldCore;
//import org.valkyrienskies.core.apigame.world.chunks.BlockType;
//import org.valkyrienskies.mod.common.BlockStateInfo;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;
//import org.valkyrienskies.mod.common.util.DimensionIdProvider;
//
//import java.util.List;
//
//@Mixin(CopycatLayerBlock.class)
//public abstract class MixinCopycatLayerBlock extends CTWaterloggedCopycatBlock implements ISpecialBlockItemRequirement {
//    @Shadow @NotNull public abstract @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext);
//
//    public MixinCopycatLayerBlock(Properties pProperties) {
//        super(pProperties);
//    }
//
//    @Unique
//    private static double vs_addition$calculateTotalVolume(final List<AABB> aabbList) {
//        double totalVolume = 0.0;
//
//        for (final AABB aabb : aabbList) {
//            final double width = aabb.maxX - aabb.minX;
//            final double height = aabb.maxY - aabb.minY;
//            final double depth = aabb.maxZ - aabb.minZ;
//            totalVolume += width * height * depth;
//        }
//
//        return totalVolume;
//    }
//
//    @Inject(
//            method = "canBeReplaced",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
//                    shift = At.Shift.BEFORE
//            )
//
//    )
//    public void canBeReplaced(BlockState pState, BlockPlaceContext pUseContext, CallbackInfoReturnable<Boolean> cir){
//        final Level pLevel = pUseContext.getLevel();
//        final BlockPos pPos = pUseContext.getClickedPos();
//
//
//        final ShipWorldCore shipObjectWorld = VSGameUtilsKt.getShipObjectWorld(pLevel);
//
//        final Double multiplier = vs_addition$calculateTotalVolume(pState.getShape(pLevel, pPos).toAabbs());
//
//        final Double Oldmass;
//
//        final Double Newmass;
//
//        final Pair<Double, BlockType> State = BlockStateInfo.INSTANCE.get(pLevel.getBlockState(pPos));
//
//        final Pair<Double, BlockType> NewState = BlockStateInfo.INSTANCE.get(pState);
//
//        if(getBlockEntity(pLevel, pPos).getMaterial()== AllBlocks.COPYCAT_BASE.getDefaultState()) {
//                Oldmass = BlockStateInfo.INSTANCE.get(Blocks.AIR.defaultBlockState()).getFirst();
//                Newmass = BlockStateInfo.INSTANCE.get(Blocks.AIR.defaultBlockState()).getFirst();
//        }
//        else {
//            Oldmass = BlockStateInfo.INSTANCE.get(getBlockEntity(pLevel, pPos).getMaterial()).getFirst() * multiplier;
//            Newmass = BlockStateInfo.INSTANCE.get(getBlockEntity(pLevel, pPos).getMaterial()).getFirst() * multiplier;
//        }
//
//        shipObjectWorld.onSetBlock(pPos.getX(), pPos.getY(), pPos.getZ(), ((DimensionIdProvider) pLevel).getDimensionId(), State.getSecond(), NewState.getSecond(), Oldmass, NewState.getFirst());
//    }
//
//}
