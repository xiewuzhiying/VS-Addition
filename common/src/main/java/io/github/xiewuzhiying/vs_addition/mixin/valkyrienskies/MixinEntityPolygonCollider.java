package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.impl.shadow.CC;

@Mixin(CC.class)
public abstract class MixinEntityPolygonCollider {
    @ModifyExpressionValue(
            method = "a(Lorg/joml/primitives/AABBdc;Lorg/joml/Vector3dc;DLjava/util/List;)Lkotlin/Pair;",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=45.0"
            ),
            require = 0,
            remap = false
    )
    private double makeThisConfigurable(double constant) {
        return VSAdditionConfig.COMMON.getMaxTiltAngle();
    }
}
