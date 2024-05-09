package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

public class CheatCannonMountPeripheral extends CannonMountPeripheral{
    public CheatCannonMountPeripheral(String type, CannonMountBlockEntity tileEntity, Level level, BlockPos blockPos) {
        super(type, tileEntity, level, blockPos);
    }

    @LuaFunction(mainThread = true)
    public final void setPitch(double value){
        if(this.isRunning())
            this.tileEntity.setPitch((float) value);
    }

    @LuaFunction(mainThread = true)
    public final void setYaw(double value){
        if(this.isRunning())
            this.tileEntity.setYaw((float) value);
    }
}
