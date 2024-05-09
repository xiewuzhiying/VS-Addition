package io.github.xiewuzhiying.vs_addition.forge.compat.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ForgePeripheralProvider implements IPeripheralProvider {
    @NotNull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        IPeripheral peripheral = (new PeripheralCommon()).getPeripheralCommon(level,blockPos);
        if(peripheral==null) peripheral = (new PeripheralForge().getPeripheralForge(level,blockPos,direction));
        if(peripheral==null) return LazyOptional.empty();
        IPeripheral finalPeripheral = peripheral;
        return LazyOptional.of(() -> finalPeripheral);
    }
}
