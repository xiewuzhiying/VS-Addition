package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.apis.IAPIEnvironment;
import dan200.computercraft.core.apis.RedstoneAPI;
import dan200.computercraft.core.computer.ComputerSide;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Mixin(RedstoneAPI.class)
public abstract class MixinRedstoneAPI{

    @Mutable
    @Final
    @Shadow(remap = false)
    private final IAPIEnvironment environment;

    public MixinRedstoneAPI(IAPIEnvironment environment) {
        this.environment = environment;
    }

    @Unique
    @LuaFunction( { "setAnalogOutput2", "setAnalogueOutput2" } )
    public final void vs_addition$setAnalogOutput2(ComputerSide side, int value ) throws LuaException
    {
        if( value < 0) throw new LuaException( "Expected number in range >= 0" );
        environment.setOutput( side, value );
    }
}
