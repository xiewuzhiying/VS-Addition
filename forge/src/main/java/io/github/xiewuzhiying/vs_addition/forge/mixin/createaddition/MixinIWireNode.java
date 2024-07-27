package io.github.xiewuzhiying.vs_addition.forge.mixin.createaddition;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mrh0.createaddition.energy.IWireNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(IWireNode.class)
public interface MixinIWireNode {
    @WrapOperation(
            method = "connect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;distSqr(Lnet/minecraft/core/Vec3i;)D"
            )
    )
    private static double man(BlockPos instance, Vec3i vec3i, Operation<Double> original, @Local(argsOnly = true) Level level) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(level, instance.getX(), instance.getY(), instance.getZ(), vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }
}
