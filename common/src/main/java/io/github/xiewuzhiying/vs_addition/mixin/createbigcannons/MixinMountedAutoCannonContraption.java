package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

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
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

import java.util.List;

@Pseudo
@Mixin(MountedAutocannonContraption.class)
public abstract class MixinMountedAutoCannonContraption extends AbstractMountedCannonContraption implements MountedAutocannonContraptionMixinDuck {

    @Unique
    private static final List<Integer> FIRE_RATES = VSAdditionConfig.SERVER.getCreateBigCannons().getCustomAutoCannonFireRates();

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
        CannonUtils.recoil(instance, x, y, z, velocity, inaccuracy, entity, VSAdditionConfig.SERVER.getCreateBigCannons().getAutoCannonRecoilForce(), original::call);
    }

    @WrapOperation(
            method = "getReferencedFireRate",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/cannons/autocannon/breech/AbstractAutocannonBreechBlockEntity;getActualFireRate()I"
            )
    )
    public int getActualFireRate(AbstractAutocannonBreechBlockEntity instance, Operation<Integer> original) {
        if (((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() < 1 || ((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() > 15) return 0;
        int cooldown = FIRE_RATES.get(((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() - 1);
        return 1200 / cooldown;
    }

    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/cannons/autocannon/breech/AbstractAutocannonBreechBlockEntity;handleFiring()V"
            )
    )
    public void handleFiring1(AbstractAutocannonBreechBlockEntity instance, Operation<Void> original) {
        if (((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() > 0 && ((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() <= VSAdditionConfig.SERVER.getCreateBigCannons().getCustomAutoCannonFireRates().toArray().length) {
            ((AbstractAutocannonBreechBlockEntityAccessor)instance).setFiringCooldown(FIRE_RATES.get(((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() - 1));
            ((AbstractAutocannonBreechBlockEntityAccessor)instance).setAnimateTicks(0);
        }
    }

    @ModifyExpressionValue(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/cannons/autocannon/breech/AbstractAutocannonBreechBlockEntity;canFire()Z"
            )
    )
    public boolean checkIsCalledByComputer(boolean original) {
        return original || this.vs_addition$isCalledByComputer;
    }

    @Inject(
            method = "fireShot",
            at = @At("RETURN")
    )
    public void reset(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci) {
        this.vs_addition$isCalledByComputer = false;
    }

    @Override
    public void vs_addition$setIsCalledByComputer() {
        this.vs_addition$isCalledByComputer = true;
    }
}
