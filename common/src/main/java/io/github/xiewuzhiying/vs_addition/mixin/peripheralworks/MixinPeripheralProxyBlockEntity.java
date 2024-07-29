package io.github.xiewuzhiying.vs_addition.mixin.peripheralworks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import site.siredvin.peripheralium.common.blockentities.MutableNBTBlockEntity;
import site.siredvin.peripheralworks.common.blockentity.PeripheralProxyBlockEntity;
import site.siredvin.peripheralworks.computercraft.peripherals.PeripheralProxyPeripheral;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(PeripheralProxyBlockEntity.class)
public abstract class MixinPeripheralProxyBlockEntity extends MutableNBTBlockEntity<PeripheralProxyPeripheral> {

    @Final
    @Shadow
    private Map<BlockPos, PeripheralProxyBlockEntity.RemotePeripheralRecord> remotePeripherals;

    public MixinPeripheralProxyBlockEntity(@NotNull BlockEntityType<?> blockEntityType, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }


    @Shadow public abstract boolean isPosApplicable(@NotNull BlockPos pos);

    @Shadow public abstract boolean removePosToTrack(@NotNull BlockPos pos);

    @WrapOperation(
            method = "isPosApplicable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            )
    )
    private boolean betweenInclShips(BlockPos instance, Vec3i vec3i, double v, Operation<Boolean> original) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(((BlockEntity)this).getLevel(), instance.getX(), instance.getY(), instance.getZ(), vec3i.getX(), vec3i.getY(), vec3i.getZ()) < Mth.square(v);
    }

    @Inject(
            method = "handleTick",
            at = @At("HEAD"),
            remap = false
    )
    private void man(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, CallbackInfo ci) {
        final List<BlockPos> needToRemove = new ArrayList<>();
        remotePeripherals.forEach((blockPos, remotePeripheralRecord) -> {
            if(!isPosApplicable(blockPos)){
                needToRemove.add(blockPos);
            }
        });
        needToRemove.forEach((this::removePosToTrack));
    }
}
