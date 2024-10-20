package io.github.xiewuzhiying.vs_addition.fabric.mixin.presencefootsteps.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.AssociationPool;
import io.github.xiewuzhiying.vs_addition.util.ShipUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

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
        final Vec3 position = entity.position();
        final BlockPos blockPos = ShipUtils.getPosStandingOnFromShips(entity.level(), new Vector3d(position.x, position.y - 1, position.z), 1);
        return original.call(
                instance,
                blockPos == null ? pos : blockPos,
                strategy
        );
    }

    @WrapOperation(
            method = "simulateBrushes",
            at = @At(
                    value = "INVOKE",
                    target = "Leu/ha3/presencefootsteps/world/AssociationPool;findAssociation(Lnet/minecraft/core/BlockPos;Ljava/lang/String;)Leu/ha3/presencefootsteps/world/Association;"
            )
    )
    private Association includeShips2(AssociationPool instance, BlockPos pos, String strategy, Operation<Association> original) {
        final Vec3 position = entity.position();
        final BlockPos blockPos = ShipUtils.getPosStandingOnFromShips(entity.level(), new Vector3d(position.x, position.y - 0.3 - (this.entity.onGround() ? 0.0 : 0.25), position.z), 1);
        return original.call(
                instance,
                blockPos == null ? pos : blockPos,
                strategy
        );
    }
}
