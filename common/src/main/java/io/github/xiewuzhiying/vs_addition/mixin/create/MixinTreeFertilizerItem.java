package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.equipment.TreeFertilizerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.world.ServerShipWorld;
import org.valkyrienskies.core.apigame.VSCore;
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet;
import org.valkyrienskies.eureka.util.ShipAssembler;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import org.valkyrienskies.core.apigame.ShipTeleportData;
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl;

import java.util.Collections;
import java.util.List;

import static org.valkyrienskies.mod.common.assembly.ShipAssemblyKt.createNewShipWithBlocks;

@Mixin(TreeFertilizerItem.class)
public abstract class MixinTreeFertilizerItem extends Item {

    public MixinTreeFertilizerItem(Properties properties) {
        super(properties);
    }

    /**
     * @author xiewuzhiying
     * @reason try to use ship instead of create new dimension
     */
    @Overwrite
    public @NotNull InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel()
                .getBlockState(context.getClickedPos());
        Block block = state.getBlock();
        if (block instanceof BonemealableBlock bonemealableBlock && state.is(BlockTags.SAPLINGS)) {

            if (context.getLevel().isClientSide) {
                BoneMealItem.addGrowthParticles(context.getLevel(), context.getClickedPos(), 100);
                return InteractionResult.SUCCESS;
            }

            BlockPos saplingPos = context.getClickedPos();
            ServerLevel world = (ServerLevel) context.getLevel();

//            DenseBlockPosSet set = new DenseBlockPosSet();
//            for (BlockPos pos : BlockPos.betweenClosed(-1, 0, -1, 1, 0, 1)) {
//                if (context.getLevel()
//                        .getBlockState(saplingPos.offset(pos))
//                        .getBlock() == block) {
//                    set.add(saplingPos.offset(pos).getX(), saplingPos.offset(pos).getY(), saplingPos.offset(pos).getZ());
//                    set.add(saplingPos.offset(pos).getX(), saplingPos.offset(pos).getY() - 1, saplingPos.offset(pos).getZ());
//                }
//            }
//
//            ServerShip ship = createNewShipWithBlocks(saplingPos, set, world);
//            ship.setStatic(true);
//
//            Vector3d position = ship.getTransform().getWorldToShip().transformPosition(VectorConversionsMCKt.toJOMLD(saplingPos));

            bonemealableBlock.performBonemeal(world, world.getRandom(),
                    saplingPos ,
                    withStage(state, 1));

//            ShipAssembler.INSTANCE.unfillShip(world, ship, Direction.SOUTH, new BlockPos(position.x, position.y, position.z), saplingPos);
//            ValkyrienSkiesMod.vsCore.deleteShips((ServerShipWorld) ship, Collections.singletonList(ship));

//            for (BlockPos pos : world.ket) {
//                BlockPos actualPos = pos.offset(saplingPos).below(10);
//                BlockState newState = world.getBlockState(pos);
//
//                // Don't replace Bedrock
//                if (context.getLevel()
//                        .getBlockState(actualPos)
//                        .getDestroySpeed(context.getLevel(), actualPos) == -1)
//                    continue;
//                // Don't replace solid blocks with leaves
//                if (!newState.isRedstoneConductor(world, pos)
//                        && !context.getLevel()
//                        .getBlockState(actualPos)
//                        .getCollisionShape(context.getLevel(), actualPos)
//                        .isEmpty())
//                    continue;
//
//                context.getLevel()
//                        .setBlockAndUpdate(actualPos, newState);
//            }

            if (context.getPlayer() != null && !context.getPlayer()
                    .isCreative())
                context.getItemInHand()
                        .shrink(1);
            return InteractionResult.SUCCESS;

        }

        return super.useOn(context);
    }

    @Shadow
    private BlockState withStage(BlockState original, int stage) {
        if (!original.hasProperty(BlockStateProperties.STAGE))
            return original;
        return original.setValue(BlockStateProperties.STAGE, 1);
    }
}
