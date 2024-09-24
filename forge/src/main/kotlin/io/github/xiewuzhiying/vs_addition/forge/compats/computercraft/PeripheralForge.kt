package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.PeripheralSupplier
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CheatCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CannonMountPeripheralWithInventory
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CheatCannonMountPeripheralWithInventory
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CheatCompactCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals.CompactCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft.InventoryMethodsAccessor
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import rbasamoyai.createbigcannons.index.CBCBlocks
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity
import riftyboi.cbcmodernwarfare.index.CBCModernWarfareBlocks

object PeripheralForge {
    private val peripheralMap: MutableMap<Block, PeripheralSupplier> = HashMap()

    init {
        if (VSAdditionMod.CBC_ACTIVE) {
            peripheralMap[CBCBlocks.CANNON_MOUNT.get()] =
                PeripheralSupplier { be: BlockEntity, _: Level, _: BlockPos ->
                    val storage = InventoryMethodsAccessor.extractHandler(be as IPeripheral)
                    if (VSAdditionConfig.SERVER.computercraft.enableCheatCannonMountPeripheral) {
                        if (storage != null) {
                            CheatCannonMountPeripheralWithInventory(
                                "cbc_cannon_mount",
                                be as CannonMountBlockEntity,
                                storage
                            )
                        } else {
                            CheatCannonMountPeripheral(
                                "cbc_cannon_mount",
                                be as CannonMountBlockEntity
                            )
                        }
                    } else {
                        if (storage != null) {
                            CannonMountPeripheralWithInventory(
                                "cbc_cannon_mount",
                                be as CannonMountBlockEntity,
                                storage
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
        if (VSAdditionMod.CBCMW_ACTIVE) {
            peripheralMap[CBCModernWarfareBlocks.COMPACT_MOUNT.get()] =
                PeripheralSupplier { be: BlockEntity, _: Level, _: BlockPos ->
                    if (VSAdditionConfig.SERVER.computercraft.enableCheatCannonMountPeripheral)
                        CheatCompactCannonMountPeripheral(
                        "cbcmf_compact_cannon_mount",
                        be as CompactCannonMountBlockEntity
                    ) else CompactCannonMountPeripheral(
                        "cbcmf_compact_cannon_mount",
                        be as CompactCannonMountBlockEntity
                    )
                }
        }
    }

    fun getPeripheralForge(level: Level, blockPos: BlockPos, direction: Direction?): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        val supplier = peripheralMap[s.block]
        return be?.let { supplier?.get(it, level, blockPos) }
    }
}