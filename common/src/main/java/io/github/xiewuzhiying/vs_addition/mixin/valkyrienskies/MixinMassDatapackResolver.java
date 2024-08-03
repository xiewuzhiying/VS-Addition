package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.config.MassDatapackResolver;
import org.valkyrienskies.physics_api.voxel.Lod1LiquidBlockState;
import org.valkyrienskies.physics_api.voxel.LodBlockBoundingBox;

@Mixin(value = MassDatapackResolver.class, remap = false)
public abstract class MixinMassDatapackResolver {
    @Mutable
    @Shadow @Final private static double DEFAULT_ELASTICITY;

    @Mutable
    @Shadow @Final private static double DEFAULT_FRICTION;

    @Mutable
    @Shadow @Final private static double DEFAULT_HARDNESS;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void configureDefaults(CallbackInfo ci) {
        DEFAULT_ELASTICITY = VSAdditionConfig.SERVER.getDefaultBlockElasticity();
        DEFAULT_FRICTION = VSAdditionConfig.SERVER.getDefaultBlockFriction();
        DEFAULT_HARDNESS = VSAdditionConfig.SERVER.getDefaultBlockHardness();
    }

    @Inject(
            method = "registerAllBlockStates",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 2
            )
    )
    private void man(Iterable<? extends BlockState> blockStates, CallbackInfo ci, @Local LodBlockBoundingBox fullLodBoundingBox, @Local(ordinal = 0) LocalRef<Lod1LiquidBlockState> waterBlockState, @Local(ordinal = 1) LocalRef<Lod1LiquidBlockState> lavaBlockState) {
        waterBlockState.set(
                new Lod1LiquidBlockState(
                        waterBlockState.get().getBoundingBox(),
                        100.0f,
                        waterBlockState.get().getDragCoefficient(),
                        waterBlockState.get().getFluidVel(),
                        waterBlockState.get().getLod1LiquidBlockStateId()
                )
        );
        lavaBlockState.set(
                new Lod1LiquidBlockState(
                        lavaBlockState.get().getBoundingBox(),
                        1000.0f,
                        lavaBlockState.get().getDragCoefficient(),
                        lavaBlockState.get().getFluidVel(),
                        lavaBlockState.get().getLod1LiquidBlockStateId()
                )
        );
    }
}
