package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork

import com.simibubi.create.foundation.placement.IPlacementHelper
import com.simibubi.create.foundation.placement.PlacementOffset
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.valkyrienskies.clockwork.ClockworkBlocks
import org.valkyrienskies.clockwork.content.physicalities.wing.FlapBlock
import org.valkyrienskies.clockwork.util.blocktype.ConnectedWingAlike
import java.util.function.Predicate

@MethodsReturnNonnullByDefault
class FlapPlacementHelper : IPlacementHelper {
    override fun getItemPredicate(): Predicate<ItemStack> {
        return Predicate { i -> ClockworkBlocks.FLAP.isIn(i) }
    }

    override fun getStatePredicate(): Predicate<BlockState> {
        return Predicate { s -> s.block is FlapBlock }
    }

    override fun getOffset(player: Player, world: Level, state: BlockState, pos: BlockPos, ray: BlockHitResult): PlacementOffset {
        val directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.location, state.getValue(ConnectedWingAlike.FACING).axis) { dir ->
            world.getBlockState(pos.relative(dir)).canBeReplaced()
        }

        return if (directions.isEmpty()) {
            PlacementOffset.fail()
        } else {
            PlacementOffset.success(pos.relative(directions[0])) { s ->
                s.setValue(ConnectedWingAlike.FACING, state.getValue(ConnectedWingAlike.FACING))
            }
        }
    }
}
