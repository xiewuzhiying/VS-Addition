package io.github.xiewuzhiying.vs_addition.forge.mixin.create;


import com.simibubi.create.foundation.events.ClientEvents;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.Link.SecondLinkRenderer;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEvents.class)
public class MixinClientEvents {
    @Inject(
            method = "onTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/redstone/link/LinkRenderer;tick()V"
            ),
            remap = false
    )
    private static void addSecondLinkRender(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        SecondLinkRenderer.tick();
    }
}
