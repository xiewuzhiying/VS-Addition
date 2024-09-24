package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft

import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.PeripheralSupplier
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CheatCompactCannonMountMethods
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CompactCannonMountMethods
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

object PeripheralForge {
    private val peripheralMap: MutableMap<Block, PeripheralSupplier> = HashMap()

    fun getPeripheralForge(level: Level, blockPos: BlockPos, direction: Direction?): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        val supplier = peripheralMap[s.block]
        return be?.let { supplier?.get(it, level, blockPos) }
    }

    @JvmStatic
    fun registerGenericPeripheralForge() {
        ComputerCraftAPI.registerGenericSource(CompactCannonMountMethods());
        ComputerCraftAPI.registerGenericSource(CheatCompactCannonMountMethods());
    }
}