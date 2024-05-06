package io.github.xiewuzhiying.vs_addition.fabric.compat.computercraft;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.peripherals.*;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon;

public class FabricPeripheralLookup extends AbstractComputerBehaviour {

    @Nullable
    public static IPeripheral peripheralProvider(Level level, BlockPos blockPos) {
        AbstractComputerBehaviour behavior = BlockEntityBehaviour.get(level, blockPos, AbstractComputerBehaviour.TYPE);
        if (behavior instanceof ComputerBehaviour real)
            return real.getPeripheral();
        return null;
    }

    IPeripheral peripheral;

    public FabricPeripheralLookup(SmartBlockEntity te) {
        super(te);
        this.peripheral = PeripheralCommon.getPeripheralCommon(te.getLevel(), te.getBlockPos());
    }

    @Override
    public <T> T getPeripheral() {
        //noinspection unchecked
        return (T) peripheral;
    }
}
