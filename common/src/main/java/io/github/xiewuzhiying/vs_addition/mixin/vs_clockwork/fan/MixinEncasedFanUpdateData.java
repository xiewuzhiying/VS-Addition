package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.fan;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.propulsion.singleton.fan.EncasedFanUpdateData;

@Pseudo
@Mixin(EncasedFanUpdateData.class)
public abstract class MixinEncasedFanUpdateData {
    @Mutable
    @Shadow(remap = false) @Final
    private double fanSpeed;

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void fanSpeedMultiplier(double fanSpeed, CallbackInfo ci) {
        this.fanSpeed = fanSpeed * VSAdditionConfig.SERVER.getClockwork().getFanForceMultiplier();
    }
}
