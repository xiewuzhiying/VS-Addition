package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.compats.createbigcannons.CannonUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMediumcannonContraption;
import riftyboi.cbcmodernwarfare.munitions.medium_cannon.AbstractMediumcannonProjectile;

@Pseudo
@Mixin(MountedMediumcannonContraption.class)
public abstract class MixinMountedMediumcannonContraption{
    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lriftyboi/cbcmodernwarfare/munitions/medium_cannon/AbstractMediumcannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractMediumcannonProjectile instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original, @Local(argsOnly = true) PitchOrientedContraptionEntity entity) {
        CannonUtils.recoil(instance, x, y, z, velocity, inaccuracy, entity, VSAdditionConfig.SERVER.getCreateBigCannons().getMediumCannonRecoilForce(), original::call);
    }
}
