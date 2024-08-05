package io.github.xiewuzhiying.vs_addition.fabric.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import eu.ha3.presencefootsteps.world.PFSolver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

@Pseudo
@Mixin(PFSolver.class)
public abstract class MixinPFSolver {
    @WrapOperation(
            method = "findAssociation(Leu/ha3/presencefootsteps/world/AssociationPool;Lnet/minecraft/world/entity/LivingEntity;DZ)Leu/ha3/presencefootsteps/world/Association;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;containing(DDD)Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos includeShips(double d, double e, double f, Operation<BlockPos> original, @Local(argsOnly = true) LivingEntity ply) {
        BlockHitResult result = RaycastUtilsKt.clipIncludeShips(ply.level(), new ClipContext(
                new Vec3(d, ply.getBoundingBox().minY - 0.001, f),
                new Vec3(d, e, f),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return result.getBlockPos();
    }
}
