package io.github.xiewuzhiying.vs_addition.mixin.waterframes.client;

import me.srrapero720.waterframes.client.display.TextureDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(TextureDisplay.class)
public abstract class MixinTextureDisplay {
    @Redirect(
            method = "limitVolume",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;m_203202_(DDD)D"
            )
    )
    private static double vs_addition$distToLowCornerSqr(BlockPos instance, double x, double y, double z) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(Minecraft.getInstance().level, instance.getX(), instance.getY(), instance.getZ(), x, y, z);
    }
}
