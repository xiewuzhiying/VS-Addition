package io.github.xiewuzhiying.vs_addition.mixin.create.deployer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import io.github.xiewuzhiying.vs_addition.compats.create.content.kinetics.deployer.DeployerScrollOptionSlot;
import io.github.xiewuzhiying.vs_addition.mixinducks.create.deployer.IDeployerBehavior;
import io.github.xiewuzhiying.vs_addition.stuff.InteractiveConditionTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Restriction(
        conflict = {
                @Condition(type = Condition.Type.TESTER, tester = InteractiveConditionTester.class)
        }
)
@Mixin(DeployerBlockEntity.class)
public abstract class MixinDeployerBlockEntity extends KineticBlockEntity implements IDeployerBehavior {

    @Unique
    protected ScrollOptionBehaviour<IDeployerBehavior.WorkigMode> workingMode;

    public MixinDeployerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(
            method = "addBehaviours",
            at = @At("RETURN"),
            remap = false
    )
    public void behaviour(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        this.workingMode = new ScrollOptionBehaviour<>(
                IDeployerBehavior.WorkigMode.class,
                Lang.translateDirect("vs_addition.working_mode"),
                (DeployerBlockEntity)(Object) this,
                vs_addition$getMovementModeSlot()
        );
        behaviours.add(this.workingMode);
    }

    @Unique
    private ValueBoxTransform vs_addition$getMovementModeSlot() {
        return new DeployerScrollOptionSlot((state, d) -> {
            Direction.Axis axis = d.getAxis();

            return axis == getSlot(state.getValue(DeployerBlock.FACING).getAxis(),
                    state.getValue(DeployerBlock.AXIS_ALONG_FIRST_COORDINATE));
        });
    }

    @Override
    public ScrollOptionBehaviour<IDeployerBehavior.WorkigMode> vs_addition$getWorkingMode() {
        return workingMode;
    }

    @Unique
    public Direction.Axis getSlot(Direction.Axis axis, boolean b) {
        switch (axis) {
            case Y -> {
                return b ? Direction.Axis.Z : Direction.Axis.X;
            }
            case X -> {
                return b ? Direction.Axis.Z : Direction.Axis.Y;
            }
            default -> {
                return b ? Direction.Axis.Y : Direction.Axis.X;
            }
        }
    }
}
