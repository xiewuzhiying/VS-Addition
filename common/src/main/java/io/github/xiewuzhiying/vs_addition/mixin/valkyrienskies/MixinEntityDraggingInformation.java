package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.EntityDraggingInformationMixinDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.valkyrienskies.mod.common.util.EntityDraggingInformation;

@Mixin(EntityDraggingInformation.class)
public abstract class MixinEntityDraggingInformation implements EntityDraggingInformationMixinDuck {
    public double addedPitchRotLastTickv = 0.0;

    @Override
    public double getAddedPitchRotLastTick() {
        return this.addedPitchRotLastTickv;
    }

    @Override
    public void setAddedPitchRotLastTick(double d) {
        this.addedPitchRotLastTickv = d;
    }
}
