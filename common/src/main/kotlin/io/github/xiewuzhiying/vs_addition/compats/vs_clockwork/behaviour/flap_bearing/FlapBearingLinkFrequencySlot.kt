package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.flap_bearing

import com.jozufozu.flywheel.util.transform.TransformStack
import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform
import com.simibubi.create.foundation.utility.AngleHelper
import com.simibubi.create.foundation.utility.VecHelper
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

open class FlapBearingLinkFrequencySlot(first: Boolean) : ValueBoxTransform.Dual(first) {
    override fun getLocalOffset(state: BlockState): Vec3 {
        val facing = state.getValue(DirectionalKineticBlock.FACING)
        var location = VecHelper.voxelSpace(16.01, 6.0, 5.5)

        if (facing.axis
                .isHorizontal
        ) {
            location = VecHelper.voxelSpace(16.01, 5.5, 6.0)
            if (isFirst) location = location.add(0.0, (5 / 16f).toDouble(), 0.0)
            return rotateHorizontally(state, location)
        }

        if (isFirst) location = location.add(0.0, 0.0, (5 / 16f).toDouble())
        location =
            VecHelper.rotateCentered(location, (if (facing == Direction.DOWN) 180 else 0).toDouble(), Direction.Axis.X)
        return location
    }

    override fun rotate(state: BlockState, ms: PoseStack) {
        val facing = state.getValue(DirectionalKineticBlock.FACING)
        val xRot: Float
        val yRot: Float
        if (facing.axis.isVertical) {
            yRot = 270f
            xRot = AngleHelper.verticalAngle(facing)
        } else {
            yRot = AngleHelper.horizontalAngle(facing) + 270
            xRot = 0f
        }
        TransformStack.cast(ms)
            .rotateX(xRot.toDouble())
            .rotateY(yRot.toDouble())
    }

    override fun getScale(): Float {
        return .4975f
    }
}