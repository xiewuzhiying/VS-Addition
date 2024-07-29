package io.github.xiewuzhiying.vs_addition.fabric.mixin.create.client;


import com.simibubi.create.foundation.events.ClientEvents;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.link.DualLinkRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(ClientEvents.class)
public class MixinClientEvents {
    @Inject(
            method = "onTick",
            at = @At("TAIL"),
            remap = false
    )
    private static void addSecondLinkRender(Minecraft client, CallbackInfo ci) {
        DualLinkRenderer.tick();
    }
}
