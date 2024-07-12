package io.github.xiewuzhiying.vs_addition.compats.create.behaviour.deployer;

import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiPredicate;

public class DeployerScrollOptionSlot extends DirectionalExtenderScrollOptionSlot {
    public DeployerScrollOptionSlot(BiPredicate<BlockState, Direction> allowedDirections) {
        super(allowedDirections);
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        return super.getLocalOffset(state)
                .add(Vec3.atLowerCornerOf(state.getValue(BlockStateProperties.FACING).getNormal()).scale(-4 / 16f));
    }
}
