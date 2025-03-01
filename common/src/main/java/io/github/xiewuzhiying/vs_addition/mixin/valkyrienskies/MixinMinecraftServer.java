package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.xiewuzhiying.vs_addition.context.VSAdditionMassDatapackResolver;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.apigame.physics.blockstates.VsBlockState;
import org.valkyrienskies.mod.common.config.MassDatapackResolver;

import java.util.Collection;

@Pseudo
@Mixin(value = MinecraftServer.class, priority = 1500)
public abstract class MixinMinecraftServer {
    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.server.MixinMinecraftServer",
            name = "postCreateLevels"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/mod/common/config/MassDatapackResolver;getRegisteredBlocks()Z",
                    remap = false
            )
    )
    private boolean modify0(boolean original) {
        return VSAdditionMassDatapackResolver.INSTANCE.getRegisteredBlocks();
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.server.MixinMinecraftServer",
            name = "postCreateLevels"
    )
    @WrapOperation(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/mod/common/config/MassDatapackResolver;registerAllBlockStates(Ljava/lang/Iterable;)V",
                    remap = false
            )
    )
    private void modify1(MassDatapackResolver instance, Iterable<BlockState> blockStates, Operation<Void> original) {
        VSAdditionMassDatapackResolver.INSTANCE.registerAllBlockStates(blockStates);
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.server.MixinMinecraftServer",
            name = "postCreateLevels"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/mod/common/config/MassDatapackResolver;getBlockStateData()Ljava/util/Collection;",
                    remap = false
            )
    )
    private Collection<VsBlockState> modify4(Collection<VsBlockState> original) {
        return VSAdditionMassDatapackResolver.INSTANCE.getBlockStateData();
    }
}
