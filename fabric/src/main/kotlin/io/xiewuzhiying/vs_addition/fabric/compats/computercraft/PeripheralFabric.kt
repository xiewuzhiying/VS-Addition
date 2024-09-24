package io.xiewuzhiying.vs_addition.fabric.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.PeripheralSupplier
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CheatCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.fabric.mixin.computercraft.InventoryMethodsAccessor
import io.xiewuzhiying.vs_addition.fabric.compats.computercraft.peripheral.CannonMountPeripheralWithInventory
import io.xiewuzhiying.vs_addition.fabric.compats.computercraft.peripheral.CheatCannonMountPeripheralWithInventory
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
                    val storage = InventoryMethodsAccessor.extractHandler(be as IPeripheral)
                    if (VSAdditionConfig.SERVER.computercraft.enableCheatCannonMountPeripheral) {
                        if (storage != null) {
                            CheatCannonMountPeripheralWithInventory(
                                "cbc_cannon_mount",
                                be as CannonMountBlockEntity,
                                InventoryMethods.StorageWrapper(storage)
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
                                InventoryMethods.StorageWrapper(storage)
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
    }

    fun getPeripheralFarbic(level: Level, blockPos: BlockPos): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        val supplier = peripheralMap[s.block]
        return be?.let { supplier?.get(it, level, blockPos) }
    }
}