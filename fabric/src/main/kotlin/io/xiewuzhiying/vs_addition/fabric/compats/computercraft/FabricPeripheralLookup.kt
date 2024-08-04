package io.xiewuzhiying.vs_addition.fabric.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

object FabricPeripheralLookup {
    fun peripheralProvider(level: Level, blockPos: BlockPos): IPeripheral? {
        return PeripheralCommon.getPeripheralCommon(level, blockPos)
    }
}