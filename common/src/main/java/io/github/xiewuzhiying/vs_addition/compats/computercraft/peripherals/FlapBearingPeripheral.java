package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.FlapBearing.FlapBearingBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

public class FlapBearingPeripheral implements IPeripheral {

    public final String type ;
    public final FlapBearingBlockEntity tileEntity;

    public FlapBearingPeripheral(String type, FlapBearingBlockEntity tileEntity, Level level, BlockPos blockPos) {
        this.type = type;
        this.tileEntity = tileEntity;
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
        if(!this.tileEntity.isRunning()){
            this.tileEntity.assemble();
            return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final boolean disassemble(){
        if(this.tileEntity.isRunning()){
            this.tileEntity.disassemble();
            return true;
        }
        return false;
    }

    @LuaFunction
    public final Object getAngle() {
        return ((FlapBearingBlockEntityAccessor)(Object)this.tileEntity).getBearingAngle();
    }
}
