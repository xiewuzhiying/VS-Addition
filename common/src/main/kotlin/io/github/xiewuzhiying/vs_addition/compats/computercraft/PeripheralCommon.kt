package io.github.xiewuzhiying.vs_addition.compats.computercraft

import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.CLOCKWORK_ACTIVE
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.EUREKA_ACTIVE
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.PeripheralSupplier
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CheatFlapBearingPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.FlapBearingPeripheral
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.ShipHelmPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import org.valkyrienskies.clockwork.ClockworkBlocks
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity
import org.valkyrienskies.eureka.EurekaBlocks
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity

object PeripheralCommon {
    private val peripheralMap: MutableMap<Block, PeripheralSupplier> = HashMap()

    init {
        if (CLOCKWORK_ACTIVE) {
            peripheralMap[ClockworkBlocks.FLAP_BEARING.get()] =
                PeripheralSupplier { be: BlockEntity, _: Level, _: BlockPos ->
                    if (VSAdditionConfig.SERVER.computercraft.enableCheatFlapBearingPeripheral) CheatFlapBearingPeripheral(
                        "clockwork_flap_bearing",
                        be as FlapBearingBlockEntity
                    ) else FlapBearingPeripheral(
                        "clockwork_flap_bearing",
                        be as FlapBearingBlockEntity
                    )
                }
        }
        if (EUREKA_ACTIVE) {
            peripheralMap[EurekaBlocks.ACACIA_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.CRIMSON_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.BIRCH_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.SPRUCE_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.WARPED_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.JUNGLE_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.OAK_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
            peripheralMap[EurekaBlocks.DARK_OAK_SHIP_HELM.get()] =
                PeripheralSupplier { be: BlockEntity, level: Level, pos: BlockPos ->
                    ShipHelmPeripheral(
                        "eureka_ship_helm",
                        be as ShipHelmBlockEntity,
                        level,
                        pos
                    )
                }
        }
    }

    @JvmStatic
    fun getPeripheralCommon(level: Level, blockPos: BlockPos): IPeripheral? {
        val s = level.getBlockState(blockPos)
        val be = level.getBlockEntity(blockPos)
        val supplier = peripheralMap[s.block]
        return be?.let { supplier?.get(it, level, blockPos) }
    }

    fun interface PeripheralSupplier {
        fun get(be: BlockEntity, level: Level, pos: BlockPos): IPeripheral?
    }
}