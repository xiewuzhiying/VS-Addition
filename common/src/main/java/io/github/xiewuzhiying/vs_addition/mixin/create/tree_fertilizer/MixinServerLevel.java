package io.github.xiewuzhiying.vs_addition.mixin.create.tree_fertilizer;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {
    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.server.world.MixinServerLevel",
            name = "onInit"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void filter(final CallbackInfo originalCi, final CallbackInfo ci) {
        if (this instanceof TreesDreamWorldAccssor) {
            ci.cancel();
        }
    }
}
