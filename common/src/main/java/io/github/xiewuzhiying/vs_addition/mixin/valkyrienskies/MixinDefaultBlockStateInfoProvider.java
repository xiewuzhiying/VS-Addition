package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.DefaultBlockStateInfoProvider;

@Pseudo
@Mixin(DefaultBlockStateInfoProvider.class)
public class MixinDefaultBlockStateInfoProvider {
    /**
     * @author xiewuzhiying
     * @reason make it configurable
     */
    @Inject(
            method = "getBlockStateMass",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    public void getBlockStateMass(BlockState blockState, CallbackInfoReturnable<Double> cir) {
        if(!blockState.isAir())
            cir.setReturnValue(VSAdditionConfig.SERVER.getDefaultBlockMass());
    }
}
