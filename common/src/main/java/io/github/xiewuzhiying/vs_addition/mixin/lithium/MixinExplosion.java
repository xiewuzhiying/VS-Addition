//package io.github.xiewuzhiying.vs_addition.mixin.lithium;
//
//import com.bawnorton.mixinsquared.TargetHandler;
//import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//import com.llamalad7.mixinextras.sugar.Local;
//import com.llamalad7.mixinextras.sugar.Share;
//import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import io.github.xiewuzhiying.vs_addition.util.ShipUtils;
//import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
//import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
//import me.fallenbreath.conditionalmixin.api.annotation.Condition;
//import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.ExplosionDamageCalculator;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.FluidState;
//import org.joml.Matrix4dc;
//import org.joml.Vector3d;
//import org.joml.primitives.AABBd;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.valkyrienskies.core.api.ships.LoadedServerShip;
//import org.valkyrienskies.core.api.ships.LoadedShip;
//import org.valkyrienskies.core.api.ships.Ship;
//import org.valkyrienskies.core.util.AABBdUtilKt;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;
//import org.valkyrienskies.mod.common.config.VSGameConfig;
//import org.valkyrienskies.mod.common.util.GameTickForceApplier;
//
//import java.util.Optional;
//import java.util.Set;
//
//
//@Pseudo
//@Restriction(
//        require = @Condition("lithium")
//)
//@Mixin(Explosion.class)
//public abstract class MixinExplosion {
//
//    @Mutable
//    @Shadow @Final private double x;
//
//    @Shadow @Final private Level level;
//
//    @Mutable
//    @Shadow @Final private double y;
//
//    @Mutable
//    @Shadow @Final private double z;
//
//    @Shadow public float radius;
//
//    @Shadow @Final private ExplosionDamageCalculator damageCalculator;
//
//    @Inject(
//            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)V",
//            at = @At("TAIL")
//    )
//    private void toWorld(Level level, Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double toBlowX, double toBlowY, double toBlowZ, float radius, boolean fire, Explosion.BlockInteraction blockInteraction, CallbackInfo ci) {
//        final Vector3d vector3d = VSGameUtilsKt.toWorldCoordinates(this.level, new Vector3d(this.x, this.y, this.z));
//        this.x = vector3d.x;
//        this.y = vector3d.y;
//        this.z = vector3d.z;
//    }
//
//    @Inject(
//            method = "explode",
//            at = @At("HEAD")
//    )
//    private void getShips(CallbackInfo ci, @Share("ships") LocalRef<Iterable<LoadedShip>> ships) {
//        ships.set(ShipUtils.getLoadedShipsIntersecting(this.level, AABBdUtilKt.expand(new AABBd(this.x, this.y, this.z, this.x, this.y, this.z), this.radius)));
//        ships.get().forEach(ship -> {
//            if (ship instanceof LoadedServerShip serverShip && serverShip.getAttachment(GameTickForceApplier.class) == null) {
//                serverShip.setAttachment(GameTickForceApplier.class, new GameTickForceApplier());
//            }
//        });
//    }
//
//    @ModifyExpressionValue(
//            method = "explode",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/Level;isInWorldBounds(Lnet/minecraft/core/BlockPos;)Z"
//            )
//    )
//    private boolean injector(boolean original, @Local(ordinal = 4) double m, @Local(ordinal = 5) double n, @Local(ordinal = 6) double o, @Local(ordinal = 0) LocalFloatRef h, @Local Set<BlockPos> set, @Share("ships")LocalRef<Iterable<Ship>> ships) {
//        boolean needsToBreak = false;
//        for (final Ship ship : ships.get()) {
//            if (!(h.get() > 0.0F)) {
//                needsToBreak = true;
//                break;
//            }
//            final Matrix4dc worldToShip = ship.getTransform().getWorldToShip();
//            final Vector3d vec31 = worldToShip.transformPosition(new Vector3d(m, n, o));
//            final BlockPos blockPos = TransformUtilsKt.getToBlockPos(vec31);
//            final BlockState blockState = this.level.getBlockState(blockPos);
//            final FluidState fluidState = this.level.getFluidState(blockPos);
//
//            final Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance((Explosion) (Object)this, this.level, blockPos, blockState, fluidState);
//            if (optional.isPresent()) {
//                h.set(h.get() - (optional.get() + 0.3F) * 0.3F);
//                final Vector3d vec32 = worldToShip.transformPosition(new Vector3d(this.x, this.y, this.z));
//
//                final Vector3d forceVec = new Vector3d(vec32).sub(vec31);
//                final double distanceMult = Math.max(0, (this.radius - forceVec.length()));
//                final double powerMult = Math.max(0.1, this.radius / 4);
//                forceVec.normalize();
//                forceVec.mul(distanceMult);
//                forceVec.mul(VSGameConfig.SERVER.getExplosionBlastForce() * 00000000.1);
//                forceVec.mul(powerMult);
//
//                final GameTickForceApplier forceApplier = ((LoadedServerShip)ship).getAttachment(GameTickForceApplier.class);
//                if (forceVec.isFinite()) {
//                    forceApplier.applyInvariantForceToPos(forceVec, vec31);
//                }
//            };
//
//            if (h.get() > 0.0F && this.damageCalculator.shouldBlockExplode((Explosion) (Object)this, this.level, blockPos, blockState, h.get())) {
//                set.add(blockPos);
//            }
//        }
//        return original && !needsToBreak;
//    }
//
//    @TargetHandler(
//            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
//            name = "performRayCast"
//    )
//    @Inject(
//            method = "@MixinSquared:Handler",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/Mth;floor(D)I"
//            )
//    )
//    private void injector(RandomSource random, double vecX, double vecY, double vecZ, LongOpenHashSet touched, CallbackInfo ci) {
//        int blockX = Mth.floor(stepX);
//        int blockY = Mth.floor(stepY);
//        int blockZ = Mth.floor(stepZ);
//        float resistance;
//        if (prevX == blockX && prevY == blockY && prevZ == blockZ) {
//            resistance = prevResistance;
//        } else {
//            if (blockY < boundMinY || blockY >= boundMaxY || blockX < -30000000 || blockZ < -30000000 || blockX >= 30000000 || blockZ >= 30000000) {
//                return;
//            }
//
//            resistance = this.traverseBlock(strength, blockX, blockY, blockZ, touched);
//            prevX = blockX;
//            prevY = blockY;
//            prevZ = blockZ;
//            prevResistance = resistance;
//        }
//    }
//}
