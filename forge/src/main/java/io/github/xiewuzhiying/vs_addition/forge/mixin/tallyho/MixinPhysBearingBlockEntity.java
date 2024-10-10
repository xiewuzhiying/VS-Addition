package io.github.xiewuzhiying.vs_addition.forge.mixin.tallyho;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.PhysBearingBlockEntity;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.data.PhysBearingUpdateData;
import org.valkyrienskies.clockwork.content.forces.contraption.BearingController;

@Pseudo
@Restriction(
        require = @Condition("tallyho")
)
@Mixin(PhysBearingBlockEntity.class)
public abstract class MixinPhysBearingBlockEntity extends GeneratingKineticBlockEntity {
    public MixinPhysBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @TargetHandler(
            mixin = "edn.stratodonut.tallyho.mixin.cw.MixinBearing",
            name = "man"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    private void undo(BearingController instance, int id, PhysBearingUpdateData data, Operation<Void> original, CallbackInfo ci) {
        original.call(instance, id, data);
        ci.cancel();
    }
}
