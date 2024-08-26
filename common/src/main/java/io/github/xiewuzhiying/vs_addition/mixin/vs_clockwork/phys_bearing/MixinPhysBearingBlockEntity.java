package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.phys_bearing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.mixinducks.vs_clockwork.phys_bearing.UpdateIsFacingNegativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.*;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.PhysBearingBlockEntity;
import org.valkyrienskies.clockwork.content.contraptions.phys.bearing.data.PhysBearingUpdateData;
import org.valkyrienskies.clockwork.content.forces.contraption.BearingController;
import net.minecraft.world.level.block.state.properties.Property;

@Pseudo
@Mixin(PhysBearingBlockEntity.class)
public abstract class MixinPhysBearingBlockEntity extends GeneratingKineticBlockEntity {
    public MixinPhysBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ModifyExpressionValue(
            method = "assemble",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=1.0E-10"
            ),
            remap = false
    )
    private double customCompliance(double constant) {
        return VSAdditionConfig.SERVER.getClockwork().getPhysBearing().getPhysBearingCompliance();
    }

    @ModifyExpressionValue(
            method = "assemble",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=1.0E10"
            ),
            remap = false
    )
    private double customMaxForce(double constant) {
        return VSAdditionConfig.SERVER.getClockwork().getPhysBearing().getPhysBearingMaxForce();
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/clockwork/content/forces/contraption/BearingController;updatePhysBearing(ILorg/valkyrienskies/clockwork/content/contraptions/phys/bearing/data/PhysBearingUpdateData;)V"
            ),
            remap = false
    )
    private void man(BearingController instance, int id, PhysBearingUpdateData data, Operation<Void> original) {
        Direction direction = (Direction)this.getBlockState().getValue((Property) BearingBlock.FACING);
        ((UpdateIsFacingNegativeDirection)(Object)instance).vs_addition$updateIsFacingNegativeDirection(
                direction == Direction.DOWN || direction == Direction.WEST || direction == Direction.NORTH
        );
        original.call(instance, id, data);
    }
}
