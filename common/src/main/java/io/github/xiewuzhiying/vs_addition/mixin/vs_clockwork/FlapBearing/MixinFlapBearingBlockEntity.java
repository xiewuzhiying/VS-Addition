package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.FlapBearing;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.Link.DualLinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing.FlapBearingLinkFrequencySlot;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing.FlapBearingLinkFrequencySlotNegative;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlock;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

import java.util.List;

@Mixin(FlapBearingBlockEntity.class)
public abstract class MixinFlapBearingBlockEntity extends KineticBlockEntity {

    @Shadow(remap = false) private BlockPos redstonePos;
    @Unique
    protected LinkBehaviour link_positive;
    @Unique
    protected DualLinkBehaviour link_negative;
    @Unique
    protected boolean receivedSignalChanged;
    @Unique
    protected int receivedSignalPositive;
    @Unique
    protected int receivedSignalNegative;
    @Unique
    protected boolean receivedSignalPositiveActive;
    @Unique
    protected boolean receivedSignalNegativeActive;

    public MixinFlapBearingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.receivedSignalPositiveActive = false;
        this.receivedSignalNegativeActive = false;
    }

    @Inject(
            method = "addBehaviours",
            at = @At("RETURN"),
            remap = false
    )
    private void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        createLink();
        behaviours.add(this.link_positive);
        behaviours.add(this.link_negative);
    }

    @Inject(
            method = "getPower",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    private void getPower(Level worldIn, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(Math.max(receivedSignalPositive, receivedSignalNegative) > cir.getReturnValue()){
            boolean isNegative = receivedSignalPositive <= receivedSignalNegative;
            Direction direction = worldIn.getBlockState(pos).getValue(FlapBearingBlock.FACING);
            Direction.Axis axis = direction.getAxis();
            if(axis.isHorizontal()) {
                Direction.Axis relativeAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                this.redstonePos = pos.relative(relativeAxis, isNegative ? 1 : -1);
            } else {
                this.redstonePos = pos.relative(Direction.Axis.X, isNegative ? 1 : -1);
            }
            cir.setReturnValue(isNegative ? receivedSignalNegative : receivedSignalPositive);
        }
    }


//    @ModifyArg(
//            method = "getFlapSpeed",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;getFlapTarget(ZZ)F"
//            ),
//            index = 0
//    )
//    private boolean modifyNegativeActivated(boolean negativeActivated) {
//        return negativeActivated || this.receivedSignalNegativeActive;
//    }
//
//    @ModifyArg(
//            method = "getFlapSpeed",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;getFlapTarget(ZZ)F"
//            ),
//            index = 1
//    )
//    private boolean modifyReceivedSignalPositive(boolean positiveActivated) {
//        return positiveActivated || this.receivedSignalPositiveActive;
//    }
//
//    @Inject(
//            method = "getFlapTarget",
//            at = @At("HEAD"),
//            cancellable = true,
//            remap = false
//    )
//    private void useReceivedSignal(boolean negativeActivated, boolean positiveActivated, CallbackInfoReturnable<Float> cir) {
//        if (negativeActivated && !positiveActivated) {
//            cir.setReturnValue((-22.5f) * (Math.max(this.redstoneLevel, this.receivedSignalNegative) / 15));
//            return;
//        }
//        if (positiveActivated && !negativeActivated) {
//            cir.setReturnValue(22.5f * (Math.max(this.redstoneLevel, this.receivedSignalPositive) / 15));
//            return;
//        }
//        cir.setReturnValue(0.0f);
//    }

    @Unique
    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> slots_positive =
                ValueBoxTransform.Dual.makeSlots(FlapBearingLinkFrequencySlot::new);
        this.link_positive = LinkBehaviour.receiver(this, slots_positive, this::setSignalPositive);
        Pair<ValueBoxTransform, ValueBoxTransform> slots_negative =
                ValueBoxTransform.Dual.makeSlots(FlapBearingLinkFrequencySlotNegative::new);
        this.link_negative = DualLinkBehaviour.receiver(this, slots_negative, this::setSignalNegative);
    }

    @Unique
    public void setSignalPositive(int power) {
        if (receivedSignalPositive != power)
            receivedSignalChanged = true;
        receivedSignalPositive = power;
        receivedSignalPositiveActive = receivedSignalPositive != 0;
    }

    @Unique
    public void setSignalNegative(int power) {
        if (receivedSignalNegative != power) {
            receivedSignalChanged = true;
        }
        receivedSignalNegative = power;
        receivedSignalNegativeActive = receivedSignalNegative != 0;
    }
}
