package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.phys_bearing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixinducks.vs_clockwork.phys_bearing.UpdateIsFacingNegativeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.data.PhysBearingData;
import org.valkyrienskies.clockwork.content.forces.contraption.BearingController;

@Pseudo
@Mixin(BearingController.class)
public abstract class MixinBearingController implements UpdateIsFacingNegativeDirection {

    @Unique
    private boolean vs_addition$isFacingNegativeDirection = false;

    @ModifyExpressionValue(
            method = "computeLockedRotationalForce",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/clockwork/content/contraptions/phys/bearing/data/PhysBearingData;getBearingAngle()D"
            ),
            remap = false
    )
    private double fixLockedMode(double original, @Local(argsOnly = true)PhysBearingData data) {
        if(this.vs_addition$isFacingNegativeDirection && (data.getBearingAxis().x() == -1.0 || data.getBearingAxis().y() == -1.0 || data.getBearingAxis().z() == -1.0)) {
            return -original;
        } else {
            return original;
        }
    }

    @ModifyVariable(
            method = "computeLockedRotationalForce",
            at = @At("STORE"),
            index = 24,
            remap = false
    )
    private double lockedModeMultiplier(double value, @Local(index = 6) double torqueMassMultiplier, @Local(index = 17) double angleErr, @Local(index = 22) double omegaErr) {
        return angleErr * torqueMassMultiplier * VSAdditionConfig.SERVER.getClockwork().getPhysBearing().getPhysBearingAngleErrorMultiplier() * 50 + omegaErr * torqueMassMultiplier * VSAdditionConfig.SERVER.getClockwork().getPhysBearing().getPhysBearingOmegaErrorMultiplier() * 50;
    }

    @ModifyVariable(
            method = "computeUnlockedRotationalForce",
            at = @At("STORE"),
            index = 7,
            remap = false
    )
    private double unlockedModeMultiplier(double value) {
        return value * VSAdditionConfig.SERVER.getClockwork().getPhysBearing().getPhysBearingOmegaErrorMultiplier();
    }

    @Override
    public void vs_addition$updateIsFacingNegativeDirection(boolean bl) {
        this.vs_addition$isFacingNegativeDirection = bl;
    }
}
