package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies.disableLogs;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Level.class, priority = 1500)
public abstract class MixinLevel {
    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.feature.get_entities.MixinLevel",
            name = "Lorg/valkyrienskies/mod/mixin/feature/get_entities/MixinLevel;lambda$check1$0(Lnet/minecraft/world/phys/AABB;)V",
            prefix = "handler"
    )
    @WrapWithCondition(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/Object;)V"
            ),
            remap = false
    )
    private boolean cancelLogging1(Logger instance, Object message) {
        return !VSAdditionConfig.SERVER.getDisableSomeWarnings();
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.feature.get_entities.MixinLevel",
            name = "Lorg/valkyrienskies/mod/mixin/feature/get_entities/MixinLevel;lambda$check2$1(Lnet/minecraft/world/phys/AABB;)V",
            prefix = "handler"
    )
    @WrapWithCondition(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/Object;)V"
            ),
            remap = false
    )
    private boolean cancelLogging2(Logger instance, Object message) {
        return !VSAdditionConfig.SERVER.getDisableSomeWarnings();
    }
}
