package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.compats.createbigcannons.CannonUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

@Pseudo
@Mixin(MountedBigCannonContraption.class)
public abstract class MixinMountedBigCannonContraption extends AbstractMountedCannonContraption {
    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractBigCannonProjectile instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original, @Local(argsOnly = true) PitchOrientedContraptionEntity entity) {
        CannonUtils.recoil(instance, x, y, z, velocity, inaccuracy, entity, VSAdditionConfig.SERVER.getCreateBigCannons().getBigCannonRecoilForce(), original::call);
    }
}
