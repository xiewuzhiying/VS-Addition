package io.github.xiewuzhiying.vs_addition.context

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.physics.blockstates.VsBlockState
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider
import org.valkyrienskies.mod.common.vsCore

class NonColliderBlockStateProvider : BlockStateInfoProvider {
    override val priority: Int
        get() = VSAdditionConfig.SERVER.nonColliderBlocksPriority

    override fun getBlockStateMass(blockState: BlockState): Double? {
        return null
    }

    override fun getBlockStateType(blockState: BlockState): BlockType? {
        return if (VSAdditionConfig.SERVER.nonColliderBlocks.contains((blockState.block.`arch$registryName`() ?: return null).toString())) {
            vsCore.blockTypes.air
        } else {
            null
        }
    }

    companion object {
        @JvmStatic
        fun register() {
            Registry.register(
                BlockStateInfo.REGISTRY,
                ResourceLocation(VSAdditionMod.MOD_ID, "data"),
                NonColliderBlockStateProvider()
            )
        }
    }
}