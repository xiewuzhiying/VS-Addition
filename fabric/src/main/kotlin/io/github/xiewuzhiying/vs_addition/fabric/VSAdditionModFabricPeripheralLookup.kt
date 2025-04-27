package io.github.xiewuzhiying.vs_addition.fabric

import dan200.computercraft.api.peripheral.PeripheralLookup
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralLookup.peripheralProvider


class VSAdditionModFabricPeripheralLookup {
    companion object {
        fun registerFallback() {
            PeripheralLookup.get().registerFallback { level: Level, blockPos: BlockPos, _: BlockState, _: BlockEntity?, _: Direction -> peripheralProvider(level, blockPos) }
        }
    }
}