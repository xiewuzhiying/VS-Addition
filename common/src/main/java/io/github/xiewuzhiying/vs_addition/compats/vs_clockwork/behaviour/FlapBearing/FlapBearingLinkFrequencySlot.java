package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlock;

public class FlapBearingLinkFrequencySlot extends ValueBoxTransform.Dual {
    public FlapBearingLinkFrequencySlot(boolean first) {
        super(first);
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction facing = state.getValue(FlapBearingBlock.FACING);
        Vec3 location = VecHelper.voxelSpace(16.01f, 6f, 5.5f);

        if (facing.getAxis()
                .isHorizontal()) {
            location = VecHelper.voxelSpace(16.01f, 5.5f, 6f);
            if (isFirst())
                location = location.add(0, 5 / 16f, 0);
            return rotateHorizontally(state, location);
        }

        if (isFirst())
            location = location.add(0, 0, 5 / 16f);
        location = VecHelper.rotateCentered(location, facing == Direction.DOWN ? 180 : 0, Direction.Axis.X);
        return location;
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = state.getValue(FlapBearingBlock.FACING);
        float xRot;
        float yRot;
        if(facing.getAxis().isVertical()) {
            yRot = 270;
            xRot = AngleHelper.verticalAngle(facing);
        } else {
            yRot = AngleHelper.horizontalAngle(facing) + 270;
            xRot = 0;
        }
        TransformStack.cast(ms)
                .rotateX(xRot)
                .rotateY(yRot);
    }

    @Override
    public float getScale() {
        return .4975f;
    }

}
