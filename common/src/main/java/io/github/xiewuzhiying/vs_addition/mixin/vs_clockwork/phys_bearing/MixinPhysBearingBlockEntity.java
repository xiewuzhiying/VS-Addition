package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.phys_bearing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.PhysBearingBlockEntity;

@Mixin(PhysBearingBlockEntity.class)
public abstract class MixinPhysBearingBlockEntity {
    @ModifyExpressionValue(
            method = "assemble",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=1.0E-10"
            ),
            remap = false
    )
    private double customCompliance(double constant) {
        return VSAdditionConfig.SERVER.getPhysBearingCompliance();
    }

    @ModifyExpressionValue(
            method = "assemble",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=1.0E10"
            ),
            remap = false
    )
    private double customMaxForce(double constant) {
        return VSAdditionConfig.SERVER.getPhysBearingMaxForce();
    }
}
