package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.flap_bearing

import com.jozufozu.flywheel.util.transform.TransformStack
import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.foundation.utility.VecHelper
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class FlapBearingLinkFrequencySlotNegative(first: Boolean) : FlapBearingLinkFrequencySlot(first) {
    override fun getLocalOffset(state: BlockState): Vec3 {
        val facing = state.getValue(DirectionalKineticBlock.FACING)
        var location = VecHelper.voxelSpace(-0.01, 6.0, 5.5)

        if (facing.axis
                .isHorizontal
        ) {
            location = VecHelper.voxelSpace(-0.01, 5.5, 6.0)
            if (isFirst) location = location.add(0.0, (5 / 16f).toDouble(), 0.0)
            return rotateHorizontally(state, location)
        }

        if (isFirst) location = location.add(0.0, 0.0, (5 / 16f).toDouble())
        location =
            VecHelper.rotateCentered(location, (if (facing == Direction.DOWN) 180 else 0).toDouble(), Direction.Axis.X)
        return location
    }

    override fun rotate(state: BlockState, ms: PoseStack) {
        super.rotate(state, ms)
        TransformStack.cast(ms).rotateY(-180.0)
    }
}
