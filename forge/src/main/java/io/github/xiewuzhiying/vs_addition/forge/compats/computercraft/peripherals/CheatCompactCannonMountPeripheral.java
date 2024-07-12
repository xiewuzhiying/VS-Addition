package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity;

public class CheatCompactCannonMountPeripheral extends CompactCannonMountPeripheral {
    public CheatCompactCannonMountPeripheral(String type, CompactCannonMountBlockEntity tileEntity, Level level, BlockPos blockPos, Direction direction) {
        super(type, tileEntity, level, blockPos, direction);
    }

    @LuaFunction(mainThread = true)
    public final void setPitch(double value) {
        if (this.isRunning())
            this.tileEntity.setPitch((float) value);
    }

    @LuaFunction(mainThread = true)
    public final void setYaw(double value) {
        if (this.isRunning())
            this.tileEntity.setYaw((float) value);
    }
}
