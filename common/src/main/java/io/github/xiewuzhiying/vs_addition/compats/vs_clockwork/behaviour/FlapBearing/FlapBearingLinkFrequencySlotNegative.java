package io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
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
        Vec3 location = VecHelper.voxelSpace(-0.01f, 6f, 5.5f);

        if (facing.getAxis()
                .isHorizontal()) {
            location = VecHelper.voxelSpace(-0.01f, 5.5f, 6f);
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
        super.rotate(state, ms);
        TransformStack.cast(ms).rotateY(-180);
    }

}
