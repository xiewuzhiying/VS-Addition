package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.EntityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.GameTickForceApplier;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

@Mixin(MountedBigCannonContraption.class)
public abstract class MixinMountedBigCannonContraption extends AbstractMountedCannonContraption {

    @Unique
    private float vs_addition$speed;

    @Unique
    private Vec3 vs_addition$vector;

    @Unique
    private ServerShip vs_addition$serverShip;

    @Inject(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;shoot(DDDFF)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void getShip(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci){
        vs_addition$serverShip = (ServerShip) VSGameUtilsKt.getShipObjectManagingPos(entity.level, VectorConversionsMCKt.toJOML(entity.getAnchorVec()));
    }

    @Redirect(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;shoot(DDDFF)V"
            )
    )
    public void shoot(AbstractBigCannonProjectile<?> instance, double x, double y, double z, float velocity, float inaccuracy) {
        vs_addition$speed = velocity;
        vs_addition$vector = (new Vec3(x, y, z)).normalize().add(((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy, ((EntityAccessor) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy, ((EntityAccessor)(Object) instance).getRandom().nextGaussian() * 0.007499999832361937 * (double)inaccuracy).scale(velocity);
        Vec3 vec3 = vs_addition$vector.add(VectorConversionsMCKt.toMinecraft(vs_addition$serverShip.getVelocity()));
        instance.setDeltaMovement(vec3);
        double d = vs_addition$vector.horizontalDistance();
        instance.setYRot((float)(Mth.atan2(vs_addition$vector.x, vs_addition$vector.z) * 57.2957763671875));
        instance.setXRot((float)(Mth.atan2(vs_addition$vector.y, d) * 57.2957763671875));
        instance.yRotO = instance.getYRot();
        instance.xRotO = instance.getXRot();
    }

    @Inject(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;shoot(DDDFF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void recoil(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci) {
        if (vs_addition$serverShip != null) {
            GameTickForceApplier applier = vs_addition$serverShip.getAttachment(GameTickForceApplier.class);
            double recoilForce = vs_addition$speed * VSAdditionConfig.SERVER.getBigCannonRecoilForce();
            applier.applyInvariantForceToPos(vs_addition$serverShip.getTransform().getShipToWorldRotation().transform(VectorConversionsMCKt.toJOML(vs_addition$vector).negate().normalize()).mul(recoilForce), VectorConversionsMCKt.toJOML(entity.getAnchorVec().add(0.5, 0.5, 0.5)).sub(vs_addition$serverShip.getTransform().getPositionInShip()));
        }
    }
}
