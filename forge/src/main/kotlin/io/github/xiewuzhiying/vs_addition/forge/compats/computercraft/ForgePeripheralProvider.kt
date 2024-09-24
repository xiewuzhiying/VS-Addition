package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.peripheral.IPeripheralProvider
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.LazyOptional

object ForgePeripheralProvider: IPeripheralProvider {
    override fun getPeripheral(level: Level, blockPos: BlockPos, direction: Direction): LazyOptional<IPeripheral> {
        val peripheral =
            PeripheralCommon.getPeripheralCommon(level, blockPos) ?:
            PeripheralForge.getPeripheralForge(level, blockPos, direction) ?:
            return LazyOptional.empty()
        return LazyOptional.of { peripheral }
    }
}