package io.github.xiewuzhiying.vs_addition.fabric.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.Solver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt.getPosStandingOnFromShips;

@Pseudo
@Mixin(targets = "eu.ha3.presencefootsteps.sound.generator.TerrestrialStepSoundGenerator")
public abstract class MixinTerrestrialStepSoundGenerator {
    @WrapOperation(
            method = "produceStep(Lnet/minecraft/world/entity/LivingEntity;Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips1(Solver instance, Level level, BlockPos blockPos, String s, Operation<Association> original, @Local(argsOnly = true) LivingEntity ply) {
        return original.call(
                instance,
                level,
                getPosStandingOnFromShips(level, new Vector3d(ply.getX(), ply.getY() - 0.2, ply.getZ()), 1),
                s);
    }

    @WrapOperation(
            method = "simulateBrushes",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips2(Solver instance, Level level, BlockPos blockPos, String s, Operation<Association> original, @Local(argsOnly = true) LivingEntity ply) {
        return original.call(
                instance,
                level,
                getPosStandingOnFromShips(level, new Vector3d(ply.getX(), ply.getY() - 0.1 - ply.getMyRidingOffset() - (ply.isOnGround() ? 0.0 : 0.25), ply.getZ()), 1),
                s);
    }
}
