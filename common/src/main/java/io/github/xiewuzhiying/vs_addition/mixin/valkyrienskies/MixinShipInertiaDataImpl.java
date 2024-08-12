package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ShipInertiaDataImplMixinDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.valkyrienskies.core.impl.game.ships.ShipInertiaDataImpl;

@Pseudo
@Mixin(ShipInertiaDataImpl.class)
public abstract class MixinShipInertiaDataImpl implements ShipInertiaDataImplMixinDuck {
    @Override
    public void addMassAt(double x, double y, double z, double mass) {
        this.addMassAt(x, y, z, mass);
    }
}
