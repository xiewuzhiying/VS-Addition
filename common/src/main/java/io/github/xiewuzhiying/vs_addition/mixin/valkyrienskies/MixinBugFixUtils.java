package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.util.BugFixUtil;

@Mixin(BugFixUtil.class)
public abstract class MixinBugFixUtils {
    @ModifyExpressionValue(
            method = "isCollisionBoxToBig",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=1000.0"
            ),
            remap = false
    )
    private double customLimit(double original) {
        return VSAdditionConfig.SERVER.getGetEntitiesAabbSizeLimit();
    }
}
