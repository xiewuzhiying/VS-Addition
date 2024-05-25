package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

@Mixin(FlapBearingBlockEntity.class)
public interface FlapBearingBlockEntityAccessor {
    @Accessor
    float getBearingAngle();
}
