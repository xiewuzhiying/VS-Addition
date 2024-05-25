package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.mixin.createbigcannons.CannonMountBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

public class CannonMountPeripheral implements IPeripheral {

    public final String type ;
    public final CannonMountBlockEntity tileEntity;

    public final Level level;

    public final BlockPos worldPosition;

    public CannonMountPeripheral(String type, CannonMountBlockEntity tileEntity, Level level, BlockPos blockPos) {
        this.type = type;
        this.tileEntity = tileEntity;
        this.level = level;
        this.worldPosition = blockPos;
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral == this;
    }

    @Override
    public Object getTarget() {
        return this.tileEntity;
    }

    @LuaFunction(mainThread = true)
    public final Object assemble(){
        if(!this.tileEntity.isRunning()) {
            ((CannonMountBlockEntityAccessor) this.tileEntity).Assemble();
            return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final Object disassemble() {
        if(this.tileEntity.isRunning()) {
            this.tileEntity.disassemble();
            this.tileEntity.sendData();
            return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final void fire() {
        if(this.tileEntity.isRunning()) {
            this.tileEntity.getContraption().tryFiringShot();
        }
    }

    @LuaFunction(mainThread = true)
    public final boolean isRunning(){
        return this.tileEntity.isRunning();
    }

//    @LuaFunction
//    public final Object getPitchOffset(IArguments partialTicks) throws LuaException {
//        if(this.isRunning()) {
//            double value = partialTicks.optDouble(0).orElse(0.0);
//            return (double) this.tileEntity.getPitchOffset((float) value);
//        }
//        return false;
//    }
//
//    @LuaFunction
//    public final Object getYawOffset(IArguments partialTicks) throws LuaException {
//        if(this.isRunning()) {
//            double value = partialTicks.optDouble(0).orElse(0.0);
//            return (double) this.tileEntity.getYawOffset((float) value);
//        }
//        return false;
//    }

    @LuaFunction
    public final double getPitch() {
        return ((CannonMountBlockEntityAccessor)this.tileEntity).getCannonPitch();
    }

    @LuaFunction
    public final double getYaw() {
        return ((CannonMountBlockEntityAccessor)this.tileEntity).getCannonYaw();
    }

    @LuaFunction
    public final Object getMaxDepress() {
        return (double) this.tileEntity.getContraption().maximumDepression();
    }

    @LuaFunction
    public final Object getMaxElevate() {
        return (double) this.tileEntity.getContraption().maximumElevation();
    }
}
