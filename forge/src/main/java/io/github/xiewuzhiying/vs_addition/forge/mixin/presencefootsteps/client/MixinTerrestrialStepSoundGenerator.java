package io.github.xiewuzhiying.vs_addition.forge.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.Solver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt.getPosStandingOnFromShips;

@Pseudo
@Mixin(targets = "eu.ha3.presencefootsteps.sound.generator.TerrestrialStepSoundGenerator")
public abstract class MixinTerrestrialStepSoundGenerator {
    @WrapOperation(
            method = "Leu/ha3/presencefootsteps/sound/generator/TerrestrialStepSoundGenerator;produceStep(Lnet/minecraft/world/entity/LivingEntity;Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            ),
            remap = false
    )
    private Association includeShips1(Solver instance, LivingEntity entity, BlockPos pos, String strategy, Operation<Association> original) {
        Vec3 position = entity.position();
        return original.call(
                instance,
                entity,
                getPosStandingOnFromShips(entity.level(), new Vector3d(position.x, position.y - 0.2, position.z)),
                strategy
        );
    }

    @WrapOperation(
            method = "produceStep(Lnet/minecraft/world/entity/LivingEntity;Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            ),
            remap = false
    )
    private Association includeShips2(Solver instance, LivingEntity entity, BlockPos pos, String strategy, Operation<Association> original) {
        Vec3 position = entity.position();
        return original.call(
                instance,
                entity,
                getPosStandingOnFromShips(entity.level(), new Vector3d(position.x, position.y - 0.2, position.z)),
                strategy
        );
    }
}
