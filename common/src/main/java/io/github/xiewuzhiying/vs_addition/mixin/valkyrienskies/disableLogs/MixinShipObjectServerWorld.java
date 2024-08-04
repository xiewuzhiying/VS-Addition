package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies.disableLogs;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.impl.game.ships.ShipObjectServerWorld;

@Pseudo
@Mixin(ShipObjectServerWorld.class)
public abstract class MixinShipObjectServerWorld {
    @WrapWithCondition(
            method = "postTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V"
            ),
            remap = false
    )
    private boolean shutUp(Logger instance, String string) {
        return !VSAdditionConfig.SERVER.getDisableSomeWarnings();
    }
}
