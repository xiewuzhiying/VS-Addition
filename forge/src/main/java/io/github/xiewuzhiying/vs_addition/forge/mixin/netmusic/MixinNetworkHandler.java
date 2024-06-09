package io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic;


import com.github.tartaricacid.netmusic.network.NetworkHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(NetworkHandler.class)
public class MixinNetworkHandler {
    @WrapOperation(
            method = "lambda$sendToNearby$3",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;distanceToSqr(DDD)D"
            ),
            remap = false
    )
    private static double redirectToWorld(ServerPlayer instance, double x, double y, double z, Operation<Double> original) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(
                instance.level(), instance.getX(), instance.getY(), instance.getZ(),
                x, y, z
        );
    }
}
