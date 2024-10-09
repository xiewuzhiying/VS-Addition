package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMunitionsLauncherContraption;

@Pseudo
@Restriction(
        require = @Condition(value = "cbcmodernwarfare", versionPredicates = "0.0.5f+mc.1.20.1-forge")
)
@Mixin(MountedMunitionsLauncherContraption.class)
public abstract class MixinMountedMunitionsLauncherContraption {
    @ModifyExpressionValue(
            method = "fireShot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;subtract(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 1
            )
    )
    private Vec3 transformToWorld(final Vec3 original, final @Local(argsOnly = true) ServerLevel level, final @Local(name = "vec") LocalRef<Vec3> vec) {
        if (VSGameUtilsKt.isBlockInShipyard(level, original)) {
            final Matrix4dc transform = VSGameUtilsKt.getShipManagingPos(level, original).getShipToWorld();
            vec.set(VectorConversionsMCKt.toMinecraft(transform.transformDirection(VectorConversionsMCKt.toJOML(vec.get()))));
            return VectorConversionsMCKt.toMinecraft(transform.transformPosition(VectorConversionsMCKt.toJOML(original)));
        }
        return original;
    }
}
