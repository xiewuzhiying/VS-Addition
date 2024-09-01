package io.github.xiewuzhiying.vs_addition.mixin.create.deployer;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
import io.github.xiewuzhiying.vs_addition.mixinducks.create.deployer.IDeployerBehavior;
import io.github.xiewuzhiying.vs_addition.stuff.InteractiveConditionTester;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

@Pseudo
@Restriction(
        conflict = {
                @Condition(type = Condition.Type.TESTER, tester = InteractiveConditionTester.class)
        }
)
@Mixin(DeployerHandler.class)
public abstract class MixinDeployerHandler {
    @ModifyExpressionValue(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;add(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 0
            )
    )
    private static Vec3 setRayOrigin(Vec3 original, @Local(argsOnly = true, ordinal = 0) DeployerFakePlayer player, @Local(argsOnly = true, ordinal = 0) Vec3 vec3, @Share("mode") LocalBooleanRef working_mode) {
        BlockEntity blockEntity = player.level().getBlockEntity(TransformUtilsKt.getToBlockPos(vec3));
        if(blockEntity != null)
            working_mode.set(((IDeployerBehavior)blockEntity).vs_addition$getWorkingMode().get() == IDeployerBehavior.WorkigMode.WITH_SHIP);
        else
            working_mode.set(false);
        if(working_mode.get())
            return VSGameUtilsKt.toWorldCoordinates(player.level(), original);
        return original;
    }

    @ModifyExpressionValue(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;add(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 1
            )
    )
    private static Vec3 setRayTarget(Vec3 original, @Local(argsOnly = true, ordinal = 0) DeployerFakePlayer player, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return VSGameUtilsKt.toWorldCoordinates(player.level(), original);
        return original;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            index = 1
    )
    private static AABB aabbToWorld(AABB par2, @Local(ordinal = 0) Level world, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return VSGameUtilsKt.transformAabbToWorld(world, par2);
        return par2;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 1
            )
    )
    private static double scale(double factor, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return factor+2/64f;
        return factor;
    }

    @WrapOperation(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;clip(Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"
            )
    )
    private static BlockHitResult clip(Level instance, ClipContext clipContext, Operation<BlockHitResult> original, @Local(argsOnly = true, ordinal = 0) Vec3 vec, @Local(argsOnly = true, ordinal = 1) Vec3 extensionVector, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get()) {
            BlockHitResult result = RaycastUtilsKt.clipIncludeShips(instance, clipContext, true);
            if (result.getType() == HitResult.Type.MISS) {
                return RaycastUtilsKt.clipIncludeShips(instance, new ClipContext(clipContext.getFrom(), VSGameUtilsKt.toWorldCoordinates(instance, vec.add(extensionVector.scale(5 / 2f - 1 / 64f))), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null), true);
            }
            return RaycastUtilsKt.clipIncludeShips(instance, clipContext, true);
        }
        return original.call(instance, clipContext);
    }

    @ModifyExpressionValue(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;"
            )
    )
    private static BlockPos redirectToTrue(BlockPos original, @Local(argsOnly = true, ordinal = 0) BlockPos clickedPos, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return clickedPos;
        return original;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            index = 0
    )
    private static BlockPos replace1(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;mayInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace2(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace3(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/BlockHelper;extinguishFire(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
                    ordinal = 0
            ),
            index = 2
    )
    private static BlockPos replace4(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;attack(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace5(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F",
                    ordinal = 0
            ),
            index = 2
    )
    private static BlockPos replace6(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace7(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/deployer/DeployerHandler;tryHarvestBlock(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerPlayerGameMode;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            ),
            index = 2
    )
    private static BlockPos replace8(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;destroyBlockProgress(ILnet/minecraft/core/BlockPos;I)V",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace9(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;destroyBlockProgress(ILnet/minecraft/core/BlockPos;I)V",
                    ordinal = 1
            ),
            index = 1
    )
    private static BlockPos replace10(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/commons/lang3/tuple/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/commons/lang3/tuple/Pair;"
            ),
            index = 0,
            remap = false
    )
    private static Object replace11(Object par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
                    ordinal = 1
            ),
            index = 1
    )
    private static BlockPos replace12(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/deployer/DeployerHandler;safeOnUse(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
            ),
            index = 2
    )
    private static BlockPos replace13(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseFireBlock;canBePlacedAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            ),
            index = 1
    )
    private static BlockPos replace14(BlockPos par1, @Local(ordinal = 0) BlockHitResult result, @Share("mode") LocalBooleanRef working_mode) {
        if(working_mode.get())
            return result.getBlockPos();
        return par1;
    }
}
