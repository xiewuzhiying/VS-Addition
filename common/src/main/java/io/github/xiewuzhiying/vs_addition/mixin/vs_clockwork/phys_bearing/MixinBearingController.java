package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.phys_bearing;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.data.PhysBearingData;
import org.valkyrienskies.clockwork.content.forces.contraption.BearingController;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

@Mixin(BearingController.class)
public abstract class MixinBearingController {
    @Inject(
            method = "computeLockedRotationalForce",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3dc;dot(Lorg/joml/Vector3dc;)D",
                    ordinal = 2
            ),
            remap = false
    )
    private void man(PhysBearingData data, PhysShipImpl physShip, PhysShipImpl otherPhysShip, CallbackInfoReturnable<Vector3dc> cir,
                     @Local(ordinal = 1) LocalDoubleRef d) {
        if (data.getBearingAxis() != null && (data.getBearingAxis().x() == -1 || data.getBearingAxis().y() == -1 || data.getBearingAxis().z() == -1))
            d.set(d.get() * -1);
    }
}
