package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.compat.CreateCompat;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMunitionsLauncherContraption;

@Pseudo
@Restriction(
        require = @Condition(value = "cbcmodernwarfare", versionPredicates = "0.0.5f+mc.1.20.1-forge")
)
@Mixin(CreateCompat.class)
public abstract class MixinCreateCompat {
    @WrapOperation(
            method = "isContraption",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Class;isInstance(Ljava/lang/Object;)Z"
            ),
            remap = false
    )
    private static boolean withOutMunitionsLauncher(Class instance, Object o, Operation<Boolean> original) {
        return original.call(instance, o) && !(o instanceof MountedMunitionsLauncherContraption);
    }
}
