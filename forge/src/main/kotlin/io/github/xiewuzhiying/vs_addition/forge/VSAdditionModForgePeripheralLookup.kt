package io.github.xiewuzhiying.vs_addition.forge

import dan200.computercraft.impl.Peripherals
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.ForgePeripheralProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class VSAdditionModForgePeripheralLookup {
    companion object {
        fun registerFallback() {
            Peripherals.register(ForgePeripheralProvider);
        }
    }
}