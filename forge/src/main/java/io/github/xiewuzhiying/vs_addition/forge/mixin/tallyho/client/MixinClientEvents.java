package io.github.xiewuzhiying.vs_addition.forge.mixin.tallyho.client;

import edn.stratodonut.tallyho.client.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(ClientEvents.class)
public abstract class MixinClientEvents {
    @Shadow(remap = false) public void onBleedout(@Coerce Object event) {
        throw new AssertionError();
    }
}
