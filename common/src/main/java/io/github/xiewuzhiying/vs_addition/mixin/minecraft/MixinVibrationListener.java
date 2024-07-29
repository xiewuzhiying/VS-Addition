package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Optional;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.xiewuzhiying.vs_addition.util.RaycastUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.User;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

@Mixin(targets = "net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Listener")
public abstract class MixinVibrationListener {
    @WrapOperation(
            method = "handleGameEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$User;canReceiveVibration(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z"
            )
    )
    private boolean betweenInclShips(final User instance, final ServerLevel serverLevel, final BlockPos blockPos, final GameEvent gameEvent, final Context context,
                                     final Operation<Boolean> original, @Local(argsOnly = true) final Vec3 arg4, final @Share("newArg4") LocalRef<Vec3> newArg4) {
        final Optional<Vec3> userVec3 = instance.getPositionSource().getPosition(serverLevel);
        final Ship userShip = userVec3.map(value -> VSGameUtilsKt.getShipManagingPos(serverLevel, value)).orElse(null);
        newArg4.set(VSGameUtilsKt.toWorldCoordinates(serverLevel, arg4));
        final BlockPos newPos = BlockPos.containing(userShip != null ? VectorConversionsMCKt.toMinecraft(userShip.getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(newArg4.get()))) : newArg4.get());
        return original.call(instance, serverLevel, newPos, gameEvent, context);
    }

    @WrapOperation(
            method = "isOccluded",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isBlockInLine(Lnet/minecraft/world/level/ClipBlockStateContext;)Lnet/minecraft/world/phys/BlockHitResult;"
            )
    )
    private static BlockHitResult man(Level instance, ClipBlockStateContext clipBlockStateContext, Operation<BlockHitResult> original) {
        return RaycastUtils.isBlockInLineIncludeShips(instance, clipBlockStateContext);
    }
}
