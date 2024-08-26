package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.EntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.GameTickForceApplier;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedRotarycannonContraption;

@Pseudo
@Mixin(MountedRotarycannonContraption.class)
public abstract class MixinMountedRotarycannonContraption {
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
                    target = "Lrbasamoyai/createbigcannons/munitions/autocannon/AbstractAutocannonProjectile;shoot(DDDFF)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void getShip(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci){
        vs_addition$serverShip = (ServerShip) VSGameUtilsKt.getShipObjectManagingPos(entity.level(), VectorConversionsMCKt.toJOML(entity.getAnchorVec()));
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
            double recoilForce = vs_addition$speed * VSAdditionConfig.SERVER.getCreateBigCannons().getRotaryCannonRecoilForce();
            applier.applyInvariantForceToPos(vs_addition$serverShip.getTransform().getShipToWorldRotation().transform(VectorConversionsMCKt.toJOML(vs_addition$vector).negate().normalize()).mul(recoilForce), VectorConversionsMCKt.toJOML(entity.getAnchorVec().add(0.5, 0.5, 0.5)).sub(vs_addition$serverShip.getTransform().getPositionInShip()));
        }
    }

    @Definition(id = "get", method = "Lcom/simibubi/create/foundation/config/ConfigBase$ConfigBool;get()Ljava/lang/Object;")
    @Definition(id = "Boolean", type = Boolean.class)
    @Expression("(Boolean) ?.get")
    @ModifyExpressionValue(
            method = "fireShot",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            require = 0,
            remap = false
    )
    private boolean fix1(boolean original, @Local AbstractAutocannonProjectile projectile) {
        return projectile != null && original;
    }

    @ModifyArgs(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lrbasamoyai/ritchiesprojectilelib/RitchiesProjectileLib;queueForceLoad(Lnet/minecraft/server/level/ServerLevel;II)V"
            ),
            require = 0,
            remap = false
    )
    private void fix2(Args args, @Local AbstractAutocannonProjectile projectile) {
        ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
        args.set(1, cpos1.x);
        args.set(2, cpos1.z);
    }
}
