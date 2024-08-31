package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CheatCompactCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CompactCannonMountPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity
import riftyboi.cbcmodernwarfare.index.CBCModernWarfareBlocks

object PeripheralForge {
    private fun c(arg1: BlockState, arg2: Block): Boolean {
        return arg1.block === arg2
    }

    fun getPeripheralForge(level: Level, blockPos: BlockPos, direction: Direction?): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        return if (VSAdditionMod.CBCMW_ACTIVE && c(s, CBCModernWarfareBlocks.COMPACT_MOUNT.get())) {
            if (VSAdditionConfig.SERVER.computercraft.enableCheatCannonMountPeripheral) CheatCompactCannonMountPeripheral(
                "cbcmf_compact_cannon_mount",
                be as CompactCannonMountBlockEntity
            ) else CompactCannonMountPeripheral(
                "cbcmf_compact_cannon_mount",
                be as CompactCannonMountBlockEntity
            )
        } else {
            null
        }
    }
}