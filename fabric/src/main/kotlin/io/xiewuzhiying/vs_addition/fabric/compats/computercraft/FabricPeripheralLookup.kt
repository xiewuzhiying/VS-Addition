package io.xiewuzhiying.vs_addition.fabric.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

object FabricPeripheralLookup {
    fun peripheralProvider(level: Level, blockPos: BlockPos?): IPeripheral? {
        val be = level.getBlockEntity(blockPos)
        if (be is IPeripheral) return be
        return null
    }

    fun peripheralProvider(be: BlockEntity?): IPeripheral? {
        if (be is IPeripheral) return be
        return null
    }
}