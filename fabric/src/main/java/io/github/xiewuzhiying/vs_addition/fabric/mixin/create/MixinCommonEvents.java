package io.github.xiewuzhiying.vs_addition.fabric.mixin.create;


import com.simibubi.create.foundation.events.CommonEvents;
import io.github.xiewuzhiying.vs_addition.fabric.compats.create.behaviour.Link.DualLinkHandler;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommonEvents.class)
public class MixinCommonEvents {
    @Inject(
            method = "register",
            at = @At("TAIL"),
            remap = false
    )
    private static void SecondLinkHandlerRegister(CallbackInfo ci) {
        UseBlockCallback.EVENT.register(DualLinkHandler::onBlockActivated);
    }
    
}
