package io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
