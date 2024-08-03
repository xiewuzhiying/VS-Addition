package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import dan200.computercraft.core.computer.computerthread.ComputerThread;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(ComputerThread.class)
public abstract class MixinComputerThread {
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/TimeUnit;toNanos(J)J"
            ),
            index = 0,
            remap = false
    )
    private static long man(long duration) {
        return VSAdditionConfig.SERVER.getComputercraft().getDefaultMinPeriod();
    }
}
