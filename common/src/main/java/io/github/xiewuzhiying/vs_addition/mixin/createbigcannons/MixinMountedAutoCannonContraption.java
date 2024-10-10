package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.EntityAccessor;
import io.github.xiewuzhiying.vs_addition.mixinducks.createbigcannons.MountedAutocannonContraptionMixinDuck;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.GameTickForceApplier;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
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
    private float vs_addition$speed;

    @Unique
    private Vec3 vs_addition$vector;

    @Unique
    private ServerShip vs_addition$serverShip;

    @Unique
    private final List<Integer> FIRE_RATES = VSAdditionConfig.SERVER.getCreateBigCannons().getCustomAutoCannonFireRates();

    @Unique
    private boolean vs_addition$isCalledByComputer = false;

    @Inject(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V"
            )
    )
    public void getShip(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci){
        vs_addition$serverShip = (ServerShip) VSGameUtilsKt.getShipObjectManagingPos(((EntityAccessor) entity).getLevel() , VectorConversionsMCKt.toJOML(entity.getAnchorVec()));
    }

    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractAutocannonProjectile instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original) {
        vs_addition$speed = velocity;
        vs_addition$vector = (new Vec3(x, y, z)).normalize().add(((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getCreateBigCannons().getSpreadMultiplier(), ((EntityAccessor)(Object) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getCreateBigCannons().getSpreadMultiplier(), ((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getCreateBigCannons().getSpreadMultiplier()).scale(velocity);
        original.call(instance,x,y,z,velocity,inaccuracy);
    }

    @Inject(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void recoil(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci) {
        if (vs_addition$serverShip != null) {
            GameTickForceApplier applier = vs_addition$serverShip.getAttachment(GameTickForceApplier.class);
            double recoilForce = vs_addition$speed * VSAdditionConfig.SERVER.getCreateBigCannons().getAutoCannonRecoilForce();
            applier.applyInvariantForceToPos(vs_addition$serverShip.getTransform().getShipToWorldRotation().transform(VectorConversionsMCKt.toJOML(vs_addition$vector).negate().normalize()).mul(recoilForce), VectorConversionsMCKt.toJOML(entity.getAnchorVec().add(0.5, 0.5, 0.5)).sub(vs_addition$serverShip.getTransform().getPositionInShip()));
        }
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
    public void setIsCalledByComputer() {
        this.vs_addition$isCalledByComputer = true;
    }
}
