package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.flap_bearing;

import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkBehaviour;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.flap_bearing.FlapBearingLinkFrequencySlot;
import io.github.xiewuzhiying.vs_addition.compats.vs_clockwork.behaviour.flap_bearing.FlapBearingLinkFrequencySlotNegative;
import io.github.xiewuzhiying.vs_addition.mixinducks.vs_clockwork.flap_bearing.FlapBearingBlockEntityMixinDuck;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlock;
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity;

import java.util.List;

@Pseudo
@Mixin(FlapBearingBlockEntity.class)
public abstract class MixinFlapBearingBlockEntity extends KineticBlockEntity implements FlapBearingBlockEntityMixinDuck {

    @Shadow(remap = false) private BlockPos redstonePos;
    @Shadow(remap = false) private float clientAngleDiff;
    @Shadow(remap = false)
    @Nullable
    private ControlledContraptionEntity flap;
    @Unique
    protected LinkBehaviour link_positive;
    @Unique
    protected DualLinkBehaviour link_negative;
    @Unique
    protected int receivedSignalPositive;
    @Unique
    protected int receivedSignalNegative;
    @Unique
    protected boolean receivedSignalPositiveActive;
    @Unique
    protected boolean receivedSignalNegativeActive;
    @Unique
    protected float lockedFlapAngle = 0.0f;
    @Unique
    protected boolean isLocked = false;


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
                Direction.Axis relativeAxis = direction.getClockWise().getAxis();
                this.redstonePos = pos.relative(relativeAxis, (isNegative ? -1 : 1) * -direction.getNormal().get(axis));
            } else {
                this.redstonePos = pos.relative(Direction.Axis.X, isNegative ? -1 : 1);
            }
            cir.setReturnValue(isNegative ? receivedSignalNegative : receivedSignalPositive);
        }
    }

    @Inject(
            method = "getFlapSpeed",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void setReturnValue(CallbackInfoReturnable<Float> cir) {
        if(this.level.isClientSide) {
            cir.setReturnValue(this.clientAngleDiff);
        }
    }

    @Inject(
            method = "lazyTick",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    public void lazyTick(CallbackInfo ci) {
        ci.cancel();
        super.lazyTick();
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;tick()V"
            ),
            remap = false
    )
    public void sendData(CallbackInfo ci) {
        if (this.flap != null) {
            Level var10000 = this.level;
            Intrinsics.checkNotNull(var10000);
            if (!var10000.isClientSide) {
                this.sendData();
            }
        }
    }

    @Inject(
            method = "getFlapTarget",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void getFlapTarget(boolean negativeActivated, boolean positiveActivated, CallbackInfoReturnable<Float> cir) {
        if (this.isLocked) {
            cir.setReturnValue(this.lockedFlapAngle);
        }
    }

    @Override
    public void setLockedFlapAngle(float angle) {
        this.lockedFlapAngle = angle;
    }

    @Override
    public float getLockedFlapAngle() {
        return this.lockedFlapAngle;
    }

    @Override
    public void setIsLocked(boolean bl) {
        this.isLocked = bl;
    }

    @Override
    public boolean getIsLocked() {
        return this.isLocked;
    }

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
        receivedSignalPositive = power;
        receivedSignalPositiveActive = receivedSignalPositive != 0;
    }

    @Unique
    public void setSignalNegative(int power) {
        receivedSignalNegative = power;
        receivedSignalNegativeActive = receivedSignalNegative != 0;
    }
}
