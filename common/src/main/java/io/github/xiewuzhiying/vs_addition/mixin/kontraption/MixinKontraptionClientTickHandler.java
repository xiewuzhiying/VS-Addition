package io.github.xiewuzhiying.vs_addition.mixin.kontraption;

import net.illuc.kontraption.client.KontraptionClientTickHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KontraptionClientTickHandler.class)
public class MixinKontraptionClientTickHandler {
    @Inject(
            method = "tickStart",
            at = @At("HEAD"),
            remap = false
    )
    public void cancelThisSpam(CallbackInfo ci) {
        ci.cancel();
    }
}
