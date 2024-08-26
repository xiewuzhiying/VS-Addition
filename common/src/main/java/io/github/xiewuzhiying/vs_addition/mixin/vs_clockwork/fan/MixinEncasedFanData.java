package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.fan;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.propulsion.singleton.fan.EncasedFanData;

@Pseudo
@Mixin(EncasedFanData.class)
public abstract class MixinEncasedFanData {
    @Mutable
    @Shadow(remap = false)
    private double fanSpeed;

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void fanSpeedMultiplier(Vector3dc fanPos, Vector3dc fanDir, double fanSpeed, CallbackInfo ci) {
        this.fanSpeed = fanSpeed * VSAdditionConfig.SERVER.getClockwork().getFanForceMultiplier();
    }
}
