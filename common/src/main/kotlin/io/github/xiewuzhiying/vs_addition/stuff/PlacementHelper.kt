package io.github.xiewuzhiying.vs_addition.stuff

import com.simibubi.create.foundation.placement.IPlacementHelper
import com.simibubi.create.foundation.placement.PlacementOffset
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Predicate

@MethodsReturnNonnullByDefault
class PlacementHelper<T, U: Comparable<U>>(val item: Item, private val clazz: Class<T>) : IPlacementHelper {
    override fun getItemPredicate(): Predicate<ItemStack> {
        return Predicate { i -> item == i.item }
    }

    override fun getStatePredicate(): Predicate<BlockState> {
        return Predicate { s -> clazz.isInstance(s.block)}
    }

    override fun getOffset(player: Player, world: Level, state: BlockState, pos: BlockPos, ray: BlockHitResult): PlacementOffset {
        val directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.location, (state.getValue(
            BlockStateProperties.FACING)).axis) { dir ->
            world.getBlockState(pos.relative(dir)).canBeReplaced()
        }

        return if (directions.isEmpty()) {
            PlacementOffset.fail()
        } else {
            PlacementOffset.success(pos.relative(directions[0])) { s ->
                s.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING))
            }
        }
    }
}
