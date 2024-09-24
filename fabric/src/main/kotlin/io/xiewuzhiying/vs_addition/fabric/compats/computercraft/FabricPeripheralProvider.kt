package io.xiewuzhiying.vs_addition.fabric.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.peripheral.IPeripheralProvider
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class FabricPeripheralProvider : IPeripheralProvider {
    override fun getPeripheral(level: Level, blockPos: BlockPos,direction: Direction
    ): IPeripheral? {
        return PeripheralCommon.getPeripheralCommon(level, blockPos) ?: PeripheralFabric.getPeripheralFarbic(level, blockPos)
    }
}