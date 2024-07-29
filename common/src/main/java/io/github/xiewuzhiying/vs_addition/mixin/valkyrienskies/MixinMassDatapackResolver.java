package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.config.MassDatapackResolver;

@Pseudo
@Mixin(value = MassDatapackResolver.class, remap = false)
public abstract class MixinMassDatapackResolver {
    @Mutable
    @Shadow @Final private static double DEFAULT_ELASTICITY;

    @Mutable
    @Shadow @Final private static double DEFAULT_FRICTION;

    @Mutable
    @Shadow @Final private static double DEFAULT_HARDNESS;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void configureDefaults(CallbackInfo ci) {
        DEFAULT_ELASTICITY = VSAdditionConfig.SERVER.getDefaultBlockElasticity();
        DEFAULT_FRICTION = VSAdditionConfig.SERVER.getDefaultBlockFriction();
        DEFAULT_HARDNESS = VSAdditionConfig.SERVER.getDefaultBlockHardness();
    }
}
