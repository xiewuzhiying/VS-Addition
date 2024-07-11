package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.EntityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.GameTickForceApplier;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

import java.util.List;

@Mixin(MountedAutocannonContraption.class)
public abstract class MixinMountedAutoCannonContraption extends AbstractMountedCannonContraption implements ItemCannon {

    @Unique
    private float vs_addition$speed;

    @Unique
    private Vec3 vs_addition$vector;

    @Unique
    private ServerShip vs_addition$serverShip;

    @Unique
    private final List<Integer> FIRE_RATES = VSAdditionConfig.SERVER.getCustomAutoCannonFireRates();

    @Inject(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void getShip(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci){
        vs_addition$serverShip = (ServerShip) VSGameUtilsKt.getShipObjectManagingPos(entity.level, VectorConversionsMCKt.toJOML(entity.getAnchorVec()));
    }


//    @Redirect(
//            method = "fireShot",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V"
//            )
//    )
//    public void shoot(AbstractAutocannonProjectile<?> instance, double x, double y, double z, float velocity, float inaccuracy) {
//        vs_addition$speed = velocity;
//        vs_addition$vector = (new Vec3(x, y, z)).normalize().add(((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy, ((EntityAccessor)(Object) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy, ((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy).scale(velocity);
//        if (vs_addition$serverShip != null)
//            vs_addition$vector = vs_addition$vector.add(VectorConversionsMCKt.toMinecraft(vs_addition$serverShip.getVelocity()));
//        instance.setDeltaMovement(vs_addition$vector);
//        double d = vs_addition$vector.horizontalDistance();
//        instance.setYRot((float)(Mth.atan2(vs_addition$vector.x, vs_addition$vector.z) * 57.2957763671875));
//        instance.setXRot((float)(Mth.atan2(vs_addition$vector.y, d) * 57.2957763671875));
//        instance.yRotO = instance.getYRot();
//        instance.xRotO = instance.getXRot();
//    }
    @WrapOperation(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractAutocannonProjectile<?> instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original) {
        vs_addition$speed = velocity;
        vs_addition$vector = (new Vec3(x, y, z)).normalize().add(((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getSpreadMultiplier(), ((EntityAccessor)(Object) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getSpreadMultiplier(), ((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy * VSAdditionConfig.SERVER.getSpreadMultiplier()).scale(velocity);
//        Vec3 vec;
//        if (vs_addition$serverShip != null){
//            Vector3d shipVelocity = vs_addition$serverShip.getVelocity().rotate(vs_addition$serverShip.getTransform().getShipToWorldRotation(), new Vector3d());
//            double vecLength = vs_addition$vector.length();
//            Vector3d vecUnit = new Vector3d(VectorConversionsMCKt.toJOML(vs_addition$vector)).div(vecLength);
//            double projectionLength = vecUnit.dot(shipVelocity);
//            Vector3d projection = new Vector3d(vecUnit).mul(projectionLength);
//            vec = vs_addition$vector.add(VectorConversionsMCKt.toMinecraft(projection));
//        } else {
//            vec = vs_addition$vector;
//        }
//        instance.setDeltaMovement(vec);
//        double d = vec.horizontalDistance();
//        instance.setYRot((float)(Mth.atan2(vec.x, vec.z) * 57.2957763671875));
//        instance.setXRot((float)(Mth.atan2(vec.y, d) * 57.2957763671875));
//        instance.yRotO = instance.getYRot();
//        instance.xRotO = instance.getXRot();
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
            double recoilForce = vs_addition$speed * VSAdditionConfig.SERVER.getAutoCannonRecoilForce();
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
        if (((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() > 0 && ((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() <= VSAdditionConfig.SERVER.getCustomAutoCannonFireRates().toArray().length) {
            ((AbstractAutocannonBreechBlockEntityAccessor)instance).setFiringCooldown(FIRE_RATES.get(((AbstractAutocannonBreechBlockEntityAccessor)instance).getFireRate() - 1));
            ((AbstractAutocannonBreechBlockEntityAccessor)instance).setAnimateTicks(0);
        }
    }
}
