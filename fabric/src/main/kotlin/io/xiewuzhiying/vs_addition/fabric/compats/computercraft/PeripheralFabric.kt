package io.xiewuzhiying.vs_addition.fabric.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.PeripheralSupplier
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CheatCannonMountPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import rbasamoyai.createbigcannons.index.CBCBlocks

object PeripheralFabric {
    private val peripheralMap: MutableMap<Block, PeripheralSupplier> = HashMap()

    init {
        if (VSAdditionMod.CBC_ACTIVE) {
            peripheralMap[CBCBlocks.CANNON_MOUNT.get()] =
                PeripheralSupplier { be: BlockEntity, _: Level, _: BlockPos ->
                    if (VSAdditionConfig.SERVER.computercraft.enableCheatCannonMountPeripheral) {
                        CheatCannonMountPeripheral(
                            "cbc_cannon_mount",
                            be as CannonMountBlockEntity
                        )
                    } else {
                        CannonMountPeripheral(
                            "cbc_cannon_mount",
                            be as CannonMountBlockEntity
                        )
                    }
                }
        }
    }

    fun getPeripheralFarbic(level: Level, blockPos: BlockPos): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        val supplier = peripheralMap[s.block]
        return be?.let { supplier?.get(it, level, blockPos) }
    }
}