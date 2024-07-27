package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.impl.game.ships.ShipInertiaDataImpl;

@Mixin(ShipInertiaDataImpl.class)
public abstract class MixinShipInertiaDataImpl {
    @Shadow public abstract Vector3dc getCenterOfMassInShip();

    @Inject(
            method = "addMassAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Vector3dc;z()D",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void fix(double x, double y, double z, double addedMass, CallbackInfo ci, @Local LocalRef<double[]> var9) {
        Vector3dc position = new Vector3d(x, y, z);
        Vector3dc center = this.getCenterOfMassInShip();

        double new_x = position.sub(center, new Vector3d()).x;
        double new_y = position.sub(center, new Vector3d()).y;
        double new_z = position.sub(center, new Vector3d()).z;

        double[] inertia = var9.get();
    }
}
