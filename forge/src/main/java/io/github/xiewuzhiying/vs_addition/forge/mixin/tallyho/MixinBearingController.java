package io.github.xiewuzhiying.vs_addition.forge.mixin.tallyho;

import com.bawnorton.mixinsquared.TargetHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.data.PhysBearingData;
import org.valkyrienskies.clockwork.content.forces.contraption.BearingController;

@Pseudo
@Restriction(
        require = @Condition("tallyho")
)
@Mixin(BearingController.class)
public abstract class MixinBearingController {
    @TargetHandler(
            mixin = "edn.stratodonut.tallyho.mixin.cw.MixinBearingController",
            name = "fixLockedMode"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    private void undo(double original, PhysBearingData data, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(original);
    }
}
