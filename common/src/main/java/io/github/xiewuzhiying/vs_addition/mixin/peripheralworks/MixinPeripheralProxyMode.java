package io.github.xiewuzhiying.vs_addition.mixin.peripheralworks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import site.siredvin.peripheralworks.common.blockentity.PeripheralProxyBlockEntity;
import site.siredvin.peripheralworks.common.configuration.PeripheralWorksConfig;
import site.siredvin.peripheralworks.subsystem.configurator.PeripheralProxyMode;

@Mixin(PeripheralProxyMode.class)
public abstract class MixinPeripheralProxyMode {

    @WrapOperation(
            method = "onBlockClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lsite/siredvin/peripheralworks/common/blockentity/PeripheralProxyBlockEntity;isPosApplicable(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean betweenInclShips(final PeripheralProxyBlockEntity instance, final BlockPos pos, final Operation<Boolean> original, final @Local(argsOnly = true) Level level) {
        final BlockPos instancePos = ((BlockEntity)instance).getBlockPos();
        if (pos == instancePos) {
            return false;
        }
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(level, instancePos.getX(), instancePos.getY(), instancePos.getZ(), pos.getX(), pos.getY(), pos.getZ()) < Mth.square(PeripheralWorksConfig.INSTANCE.getPeripheralProxyMaxRange());
    }
}
