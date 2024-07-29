package io.github.xiewuzhiying.vs_addition.mixin.peripheralworks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import site.siredvin.peripheralworks.common.blockentity.PeripheralProxyBlockEntity;

@Mixin(PeripheralProxyBlockEntity.class)
public abstract class MixinPeripheralProxyBlockEntity {

    @WrapOperation(
            method = "isPosApplicable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            )
    )
    private boolean betweenInclShips(BlockPos instance, Vec3i vec3i, double v, Operation<Boolean> original) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(((PeripheralProxyBlockEntity)(Object)this).getLevel(), instance.getX(), instance.getY(), instance.getZ(), vec3i.getX(), vec3i.getY(), vec3i.getZ()) < Mth.square(v);
    }
}
