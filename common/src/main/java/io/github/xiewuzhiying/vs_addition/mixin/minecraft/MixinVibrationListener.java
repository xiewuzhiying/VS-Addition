//package io.github.xiewuzhiying.vs_addition.mixin.minecraft;
//
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.llamalad7.mixinextras.sugar.Local;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import io.github.xiewuzhiying.vs_addition.util.RaycastUtils;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.ClipBlockStateContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.gameevent.GameEvent;
//import net.minecraft.world.level.gameevent.GameEvent.Context;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.Vec3;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.valkyrienskies.core.api.ships.Ship;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;
//import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
//
//@Mixin(targets = "net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Listener")
//public abstract class MixinVibrationListener {
//    @Inject(
//            method = "handleGameEvent",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$User;canReceiveVibration(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z"
//            )
//    )
//    private void betweenInclShips(ServerLevel level, GameEvent arg2, Context arg3, Vec3 arg4, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0, argsOnly = true) final LocalRef<Vec3> vec3, @Local(ordinal = 1) final Vec3 vec32) {
//        final Ship userShip = VSGameUtilsKt.getShipManagingPos(level, vec32);
//        if(userShip != null) {
//            vec3.set(VectorConversionsMCKt.toMinecraft(userShip.getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(vec3.get()))));
//        }
//    }
//
//    @WrapOperation(
//            method = "isOccluded",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/Level;isBlockInLine(Lnet/minecraft/world/level/ClipBlockStateContext;)Lnet/minecraft/world/phys/BlockHitResult;"
//            )
//    )
//    private static BlockHitResult man(Level level, ClipBlockStateContext clipBlockStateContext, Operation<BlockHitResult> original) {
//        return RaycastUtils.isBlockInLineIncludeShips(level, new ClipBlockStateContext(VSGameUtilsKt.toWorldCoordinates(level, clipBlockStateContext.getFrom()), VSGameUtilsKt.toWorldCoordinates(level, clipBlockStateContext.getTo()), clipBlockStateContext.isTargetBlock()));
//    }
//}
