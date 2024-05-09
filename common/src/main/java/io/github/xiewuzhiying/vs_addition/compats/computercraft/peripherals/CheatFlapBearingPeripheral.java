package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

public class CheatFlapBearingPeripheral extends FlapBearingPeripheral{
    public CheatFlapBearingPeripheral(String type, FlapBearingBlockEntity tileEntity, Level level, BlockPos blockPos) {
        super(type, tileEntity, level, blockPos);
    }

    @LuaFunction(mainThread = true)
    public final Object setAngle(double angle){
        if(this.tileEntity.isRunning()) {
            this.tileEntity.setAngle((float) angle);
            return true;
        }
        return false;
    }
}
