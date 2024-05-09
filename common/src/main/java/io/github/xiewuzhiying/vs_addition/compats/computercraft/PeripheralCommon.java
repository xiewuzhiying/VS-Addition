package io.github.xiewuzhiying.vs_addition.compats.computercraft;


import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.valkyrienskies.clockwork.ClockworkBlocks;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;
import org.valkyrienskies.eureka.EurekaBlocks;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PeripheralCommon {
    private final Map<Block, PeripheralSupplier> peripheralMap = new HashMap<>();

    public PeripheralCommon() {
        if (VSAdditionMod.getCBC_ACTIVE()) {
            peripheralMap.put(CBCBlocks.CANNON_MOUNT.get(), (be, level, pos) -> {
                if (VSAdditionConfig.SERVER.getEnableCheatCannonMountPeripheral()) {
                    return new CheatCannonMountPeripheral("cbc_cannon_mount", (CannonMountBlockEntity) be, level, pos);
                } else {
                    return new CannonMountPeripheral("cbc_cannon_mount", (CannonMountBlockEntity) be, level, pos);
                }
            });
        }
        if (VSAdditionMod.getCLOCKWORK_ACTIVE()) {
            peripheralMap.put(ClockworkBlocks.FLAP_BEARING.get(), (be, level, pos) -> VSAdditionConfig.SERVER.getEnableCheatFlapBearingPeripheral() ? new CheatFlapBearingPeripheral("clockwork_flap_bearing", (FlapBearingBlockEntity) be, level, pos) : new FlapBearingPeripheral("clockwork_flap_bearing", (FlapBearingBlockEntity) be, level, pos));
        }
        if (VSAdditionMod.getEUREKA_ACTIVE()) {
            peripheralMap.put(EurekaBlocks.INSTANCE.getACACIA_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getCRIMSON_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getBIRCH_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getSPRUCE_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getWARPED_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getJUNGLE_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getOAK_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
            peripheralMap.put(EurekaBlocks.INSTANCE.getDARK_OAK_SHIP_HELM().get(), (be, level, pos) -> { return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, pos); });
        }
    }

    @Nullable
    public IPeripheral getPeripheralCommon(Level level, BlockPos blockPos){
        BlockState s = level.getBlockState(blockPos);
        BlockEntity be = level.getBlockEntity(blockPos);
        PeripheralSupplier supplier = peripheralMap.get(s.getBlock());
        return supplier != null ? supplier.get(be, level, blockPos) : null;
    }

    interface PeripheralSupplier {
        IPeripheral get(BlockEntity be, Level level, BlockPos pos);
    }
}
