package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing.FlapBearingLinkFrequencySlot;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.FlapBearing.FlapBearingLinkFrequencySlotNegative;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

import java.util.List;

@Mixin(FlapBearingBlockEntity.class)
public abstract class MixinFlapBearingBlockEntity extends KineticBlockEntity {

    @Shadow(remap = false) private int redstoneLevel;
    @Unique
    protected LinkBehaviour link_positive;
    @Unique
    protected LinkBehaviour link_negative;
    @Unique
    protected boolean receivedSignalChanged;
    @Unique
    protected int receivedSignalPositive;
    @Unique
    protected int receivedSignalNegative;

    public MixinFlapBearingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(
            method = "addBehaviours",
            at = @At("RETURN"),
            remap = false
    )
    private void addBehaviour(List<LinkBehaviour> behaviours, CallbackInfo ci) {
        createLink();
        behaviours.add(this.link_positive);
        behaviours.add(this.link_negative);
    }

    @ModifyArg(
            method = "getFlapSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;getFlapTarget(ZZ)F"
            ),
            index = 0
    )
    private boolean modifyNegativeActivated(boolean negativeActivated) {
        return negativeActivated || this.receivedSignalNegative!=0;
    }

    @ModifyArg(
            method = "getFlapSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/clockwork/content/contraptions/flap/FlapBearingBlockEntity;getFlapTarget(ZZ)F"
            ),
            index = 1
    )
    private boolean modifyReceivedSignalPositive(boolean positiveActivated) {
        return positiveActivated || this.receivedSignalPositive!=0;
    }

    @Inject(
            method = "getFlapTarget",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void useReceivedSignal(boolean negativeActivated, boolean positiveActivated, CallbackInfoReturnable<Float> cir) {
        if (negativeActivated && !positiveActivated) {
            cir.setReturnValue((-22.5f) * (Math.max(this.redstoneLevel, this.receivedSignalNegative) / 15));
            return;
        }
        if (positiveActivated && !negativeActivated) {
            cir.setReturnValue(22.5f * (Math.max(this.redstoneLevel, this.receivedSignalPositive) / 15));
            return;
        }
        cir.setReturnValue(0.0f);
    }

    @Unique
    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> slots_positive =
                ValueBoxTransform.Dual.makeSlots(FlapBearingLinkFrequencySlot::new);
        this.link_positive = LinkBehaviour.receiver(this, slots_positive, this::setSignalPositive);
        Pair<ValueBoxTransform, ValueBoxTransform> slots_negative =
                ValueBoxTransform.Dual.makeSlots(FlapBearingLinkFrequencySlotNegative::new);
        this.link_negative = LinkBehaviour.receiver(this, slots_negative, this::setSignalNegative);
    }

    @Unique
    public void setSignalPositive(int power) {
        if (receivedSignalPositive != power)
            receivedSignalChanged = true;
        receivedSignalPositive = power;
    }

    @Unique
    public void setSignalNegative(int power) {
        if (receivedSignalNegative != power)
            receivedSignalChanged = true;
        receivedSignalNegative = power;
    }
}
