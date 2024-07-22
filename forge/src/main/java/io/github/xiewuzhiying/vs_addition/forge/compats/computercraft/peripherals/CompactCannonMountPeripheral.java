package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare.CompactCannonMountBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity;

public class CompactCannonMountPeripheral implements IPeripheral {

    public final String type;
    public final CompactCannonMountBlockEntity tileEntity;

    public final Level level;

    public final BlockPos worldPosition;

    public final Direction direction;

    public CompactCannonMountPeripheral(String type, CompactCannonMountBlockEntity tileEntity, Level level, BlockPos blockPos, Direction direction) {
        this.type = type;
        this.tileEntity = tileEntity;
        this.level = level;
        this.worldPosition = blockPos;
        this.direction = direction;
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
    public final Object assemble() {
        if (!this.tileEntity.isRunning()) {
            ((CompactCannonMountBlockEntityAccessor) this.tileEntity).Assemble();
            return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final Object disassemble() {
        if (this.tileEntity.isRunning()) {
            this.tileEntity.disassemble();
            this.tileEntity.sendData();
            return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final void fire() {
        if (this.tileEntity.isRunning()) {
            this.tileEntity.getContraption().tryFiringShot();
        }
    }

    @LuaFunction(mainThread = true)
    public final boolean isRunning() {
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
        return ((CompactCannonMountBlockEntityAccessor) this.tileEntity).getCannonPitch();
    }

    @LuaFunction
    public final double getYaw() {
        return ((CompactCannonMountBlockEntityAccessor) this.tileEntity).getCannonYaw();
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
