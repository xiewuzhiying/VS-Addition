package io.github.xiewuzhiying.vs_addition.forge.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.Solver;
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
@Mixin(targets = "eu.ha3.presencefootsteps.sound.generator.TerrestrialStepSoundGenerator")
public abstract class MixinTerrestrialStepSoundGenerator {
    @WrapOperation(
            method = "Leu/ha3/presencefootsteps/sound/generator/TerrestrialStepSoundGenerator;produceStep(Lnet/minecraft/world/entity/LivingEntity;Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips1(Solver instance, BlockPos pos, String strategy, Operation<Association> original, @Local(argsOnly = true) LivingEntity entity) {
        Vec3 vec3 = new Vec3(entity.getX(), entity.getBoundingBox().minY -0.001, entity.getZ());
        BlockHitResult result = RaycastUtilsKt.clipIncludeShips(entity.level(), new ClipContext(
                vec3,
                vec3.add(0.0, -0.1, 0.0),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return original.call(instance, result.getBlockPos(), strategy);
    }

    @WrapOperation(
            method = "produceStep(Lnet/minecraft/world/entity/LivingEntity;Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/Solver;findAssociation(Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips2(Solver instance, BlockPos pos, String strategy, Operation<Association> original, @Local(argsOnly = true) LivingEntity entity) {
        Vec3 vec3 = new Vec3(entity.getX(), entity.getBoundingBox().minY -0.001, entity.getZ());
        BlockHitResult result = RaycastUtilsKt.clipIncludeShips(entity.level(), new ClipContext(
                vec3,
                vec3.add(0.0, -0.1, 0.0),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return original.call(instance, result.getBlockPos(), strategy);
    }
}
