package io.github.xiewuzhiying.vs_addition.fabric.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.AssociationPool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

@Pseudo
@Mixin(targets = "eu.ha3.presencefootsteps.sound.generator.TerrestrialStepSoundGenerator")
public abstract class MixinTerrestrialStepSoundGenerator {
    @Shadow @Final protected LivingEntity entity;

    @WrapOperation(
            method = "produceStep(Leu/ha3/presencefootsteps/sound/State;D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/AssociationPool;findAssociation(Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips1(AssociationPool instance, BlockPos pos, String strategy, Operation<Association> original) {
        Vec3 vec3 = new Vec3(this.entity.getX(), this.entity.getBoundingBox().minY -0.001, this.entity.getZ());
        BlockHitResult result = RaycastUtilsKt.clipIncludeShips(this.entity.level(), new ClipContext(
                vec3,
                vec3.add(0.0, -0.1, 0.0),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return original.call(instance, result.getBlockPos(), strategy);
    }

    @WrapOperation(
            method = "simulateBrushes",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/AssociationPool;findAssociation(Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips2(AssociationPool instance, BlockPos pos, String strategy, Operation<Association> original) {
        Vec3 vec3 = new Vec3(this.entity.getX(), this.entity.getBoundingBox().minY -0.001, this.entity.getZ());
        BlockHitResult result = RaycastUtilsKt.clipIncludeShips(this.entity.level(), new ClipContext(
                vec3,
                vec3.add(0.0, -0.1, 0.0),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return original.call(instance, result.getBlockPos(), strategy);
    }
}
