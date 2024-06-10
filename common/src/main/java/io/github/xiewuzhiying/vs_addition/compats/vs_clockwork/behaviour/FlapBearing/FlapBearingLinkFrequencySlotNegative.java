package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlock;

public class FlapBearingLinkFrequencySlotNegative extends FlapBearingLinkFrequencySlot {
    public FlapBearingLinkFrequencySlotNegative(boolean first) {
        super(first);
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction facing = state.getValue(FlapBearingBlock.FACING);
        Vec3 location = VecHelper.voxelSpace(16.01f, 10f, 5.5f);

        if (facing.getAxis()
                .isHorizontal()) {
            location = VecHelper.voxelSpace(16.01f, 5.5f, 10f);
            if (isFirst())
                location = location.add(0, 5 / 16f, 0);
            location = VecHelper.rotateCentered(location, 180, Direction.Axis.Y);
            return rotateHorizontally(state, location);
        }

        if (isFirst())
            location = location.add(0, 0, 5 / 16f);
        location = VecHelper.rotateCentered(location, facing == Direction.DOWN ? 0 : 180, Direction.Axis.X);
        return location;
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = state.getValue(FlapBearingBlock.FACING);
        float yRot = facing.getAxis()
                .isVertical() ? -90 : AngleHelper.horizontalAngle(facing) + -270;
        TransformStack.cast(ms)
                .rotateY(yRot);
    }
}