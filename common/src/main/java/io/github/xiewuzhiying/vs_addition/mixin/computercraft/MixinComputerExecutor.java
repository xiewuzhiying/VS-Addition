package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "dan200.computercraft.core.computer.ComputerExecutor")
public abstract class MixinComputerExecutor {
    @ModifyExpressionValue(
            method = "queueEvent",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=256"
            ),
            remap = false
    )
    private int customMaxSize(int original) {
        return VSAdditionConfig.SERVER.getComputercraft().getEventQueueMaxSize();
    }
}
