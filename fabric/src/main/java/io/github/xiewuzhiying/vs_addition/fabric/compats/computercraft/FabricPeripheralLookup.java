package io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class FabricPeripheralLookup {
    @Nullable
    public static IPeripheral peripheralProvider(Level level, BlockPos blockPos) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof IPeripheral real)
            return real;
        return null;
    }

    @Nullable
    public static IPeripheral peripheralProvider(BlockEntity be) {
        if (be instanceof IPeripheral real)
            return real;
        return null;
    }
}
