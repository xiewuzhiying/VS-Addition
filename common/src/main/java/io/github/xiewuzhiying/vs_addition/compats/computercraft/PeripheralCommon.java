package io.github.xiewuzhiying.vs_addition.compats.computercraft;


import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals.CannonMountPeripheral;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals.FlapBearingPeripheral;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals.ShipHelmPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class PeripheralCommon {
    private static boolean c(BlockState arg1, Block arg2) { return arg1.getBlock() == arg2; }

    @Nullable
    public static IPeripheral getPeripheralCommon(Level level, BlockPos blockPos){
        BlockState s = level.getBlockState(blockPos);
        BlockEntity be = level.getBlockEntity(blockPos);
        if (VSAdditionMod.getCBC_ACTIVE() && c(s, CBCBlocks.CANNON_MOUNT.get())) {
            return VSAdditionConfig.SERVER.getEnableCheatCannonMountPeripheral() ? new CheatCannonMountPeripheral("cbc_cannon_mount", (CannonMountBlockEntity) be, level, blockPos) : new CannonMountPeripheral("cbc_cannon_mount", (CannonMountBlockEntity) be, level, blockPos);
        } else if (VSAdditionMod.getCLOCKWORK_ACTIVE() && c(s, ClockworkBlocks.FLAP_BEARING.get())) {
            return VSAdditionConfig.SERVER.getEnableCheatFlapBearingPeripheral() ? new CheatFlapBearingPeripheral("clockwork_flap_bearing", (FlapBearingBlockEntity) be, level, blockPos) : new FlapBearingPeripheral("clockwork_flap_bearing", (FlapBearingBlockEntity) be, level, blockPos);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getACACIA_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getCRIMSON_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getBIRCH_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getSPRUCE_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getWARPED_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getJUNGLE_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getOAK_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else if (VSAdditionMod.getEUREKA_ACTIVE() && c(s, EurekaBlocks.INSTANCE.getDARK_OAK_SHIP_HELM().get())) {
            return new ShipHelmPeripheral("eureka_ship_helm", (ShipHelmBlockEntity) be, level, blockPos, direction);
        } else {
            return null;
        }
    }
}
