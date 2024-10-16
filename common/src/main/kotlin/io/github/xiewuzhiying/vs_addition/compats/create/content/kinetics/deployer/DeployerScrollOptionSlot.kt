package io.github.xiewuzhiying.vs_addition.compats.create.content.kinetics.deployer

import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3
import java.util.function.BiPredicate

class DeployerScrollOptionSlot(allowedDirections: BiPredicate<BlockState, Direction>) :
    DirectionalExtenderScrollOptionSlot(allowedDirections) {
    override fun getLocalOffset(state: BlockState): Vec3 {
        return super.getLocalOffset(state)
            .add(Vec3.atLowerCornerOf(state.getValue(BlockStateProperties.FACING).normal).scale((-4 / 16f).toDouble()))
    }
}