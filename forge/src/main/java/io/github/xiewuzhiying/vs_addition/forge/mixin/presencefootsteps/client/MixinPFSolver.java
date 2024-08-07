package io.github.xiewuzhiying.vs_addition.forge.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.sugar.Local;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.PFSolver;
import eu.ha3.presencefootsteps.world.Solver;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Pseudo
@Mixin(PFSolver.class)
public abstract class MixinPFSolver {
    @Shadow
    @Final
    private static double TRAP_DOOR_OFFSET;
    @Inject(
            method = "findAssociation(Lnet/minecraft/world/entity/LivingEntity;DZ)Leu/ha3/presencefootsteps/world/Association;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void includeShips(PFSolver instance, LivingEntity ply, double verticalOffsetAsMinus, boolean isRightFoot, CallbackInfoReturnable<Association> cir, @Local(ordinal = 1) double rot, @Local Vec3 pos, @Local float feetDistanceToCenter) {
        cir.setReturnValue(new Association(ply, TransformUtilsKt.getPosStandingOnFromShips(ply.level, (new Vector3d(pos.x  + (Math.cos(rot) * feetDistanceToCenter), (ply.getBoundingBox().min(Direction.Axis.Y) - TRAP_DOOR_OFFSET) - verticalOffsetAsMinus, pos.z + (Math.sin(rot) * feetDistanceToCenter))))));
    }
}
