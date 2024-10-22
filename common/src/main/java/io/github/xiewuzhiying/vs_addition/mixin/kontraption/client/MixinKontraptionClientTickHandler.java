package io.github.xiewuzhiying.vs_addition.mixin.kontraption.client;

import net.illuc.kontraption.client.KontraptionClientTickHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(KontraptionClientTickHandler.class)
public class MixinKontraptionClientTickHandler {
    @Inject(
            method = "onTick",
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void cancelThisSpam(CallbackInfo ci) {
        ci.cancel();
    }
}
