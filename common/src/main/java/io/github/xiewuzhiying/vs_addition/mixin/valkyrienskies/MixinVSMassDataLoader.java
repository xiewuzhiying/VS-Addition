package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.valkyrienskies.mod.common.DefaultBlockStateInfoProvider;

@Mixin(DefaultBlockStateInfoProvider.class)
public class MixinVSMassDataLoader {
    /**
     * @author xiewuzhiying
     * @reason make it configurable
     */
    @Overwrite
    public @NotNull Double getBlockStateMass(BlockState blockState) {
        if(blockState.isAir()) return 0.0;
        return VSAdditionConfig.SERVER.getDefaultBlockMass();
    }
}
