package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import static com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity.getRange;

@Mixin(ArmBlockEntity.class)
public abstract class MixinArmBlockEntity extends KineticBlockEntity implements ITransformableBlockEntity {

    public MixinArmBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @WrapOperation(
            method = "searchForItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPoint;isValid()Z"
            ),
            remap = false
    )
    public boolean searchForItem(ArmInteractionPoint instance, Operation<Boolean> original, @Local ArmInteractionPoint armInteractionPoint) {
        Vector3d armPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()));
        Vector3d pointPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(instance.getPos().getX(), instance.getPos().getY(), instance.getPos().getZ()));
        if(VSGameUtilsKt.squaredDistanceBetweenInclShips(getLevel(), armPos.x, armPos.y, armPos.z, pointPos.x, pointPos.y, pointPos.z) > Mth.square(getRange()))
            return false;
        return original.call(instance);
    }


    @WrapOperation(
            method = "searchForDestination",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPoint;isValid()Z"
            ),
            remap = false
    )
    public boolean searchForDestination(ArmInteractionPoint instance, Operation<Boolean> original, @Local ArmInteractionPoint armInteractionPoint) {
        Vector3d armPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()));
        Vector3d pointPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(instance.getPos().getX(), instance.getPos().getY(), instance.getPos().getZ()));
        if(armPos.distanceSquared(pointPos) > Mth.square(getRange()))
            return false;
        return original.call(instance);
    }

    @Inject(
            method = "lazyTick",
            at = @At("HEAD"),
            remap = false
    )
    public void updatePoints(CallbackInfo ci){
        ((ArmBlockEntityAccessor) this).setUpdateInteractionPoints(true);
    }
}
