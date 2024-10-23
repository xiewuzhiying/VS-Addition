package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.compats.createbigcannons.CannonUtils;
import io.github.xiewuzhiying.vs_addition.mixinducks.createbigcannons.MountedAutocannonContraptionMixinDuck;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedRotarycannonContraption;

@Pseudo
@Mixin(MountedRotarycannonContraption.class)
public abstract class MixinMountedRotarycannonContraption implements MountedAutocannonContraptionMixinDuck {

    @Unique
    private boolean vs_addition$isCalledByComputer = false;

    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractAutocannonProjectile instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original, @Local(argsOnly = true) PitchOrientedContraptionEntity entity) {
        CannonUtils.recoil(instance, x, y, z, velocity, inaccuracy, entity, VSAdditionConfig.SERVER.getCreateBigCannons().getRotaryCannonRecoilForce(), original::call);
    }

    @ModifyExpressionValue(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lriftyboi/cbcmodernwarfare/cannons/rotarycannon/breech/AbstractRotarycannonBreechBlockEntity;canFire()Z"
            ),
            remap = false
    )
    public boolean checkIsCalledByComputer(boolean original) {
        return original || this.vs_addition$isCalledByComputer;
    }

    @Inject(
            method = "fireShot",
            at = @At("RETURN"),
            remap = false
    )
    public void reset(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci) {
        this.vs_addition$isCalledByComputer = false;
    }

    @Override
    public void vs_addition$setIsCalledByComputer() {
        this.vs_addition$isCalledByComputer = true;
    }
}
