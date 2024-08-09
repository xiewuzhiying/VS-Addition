package io.github.xiewuzhiying.vs_addition.forge.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.sugar.Local;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.PFSolver;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(PFSolver.class)
public abstract class MixinPFSolver {
    @Shadow(remap = false)
    protected abstract Association findAssociation(Entity player, BlockPos pos);

    @Inject(
            method = "findAssociation(Lnet/minecraft/world/entity/LivingEntity;DZ)Leu/ha3/presencefootsteps/world/Association;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void includeShips(LivingEntity ply, double verticalOffsetAsMinus, boolean isRightFoot, CallbackInfoReturnable<Association> cir, @Local(ordinal = 1) double rot, @Local Vec3 pos, @Local float feetDistanceToCenter) {
        BlockPos blockPos = TransformUtilsKt.getPosStandingOnFromShips(ply.level, (new Vector3d(pos.x  + (Math.cos(rot) * feetDistanceToCenter), (ply.getBoundingBox().min(Direction.Axis.Y) - 0.1) - verticalOffsetAsMinus, pos.z + (Math.sin(rot) * feetDistanceToCenter))), 1);
        cir.setReturnValue(findAssociation(ply, blockPos));
    }
}
