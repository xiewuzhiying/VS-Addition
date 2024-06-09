package io.github.xiewuzhiying.vs_addition.mixin.create.Deployer;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

@Mixin(DeployerHandler.class)
public class MixinDeployerHandler {
    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            index = 1
    )
    private static AABB aabbToWorld(AABB par2, @Local(ordinal = 0) ServerLevel world) {
        return VSGameUtilsKt.transformAabbToWorld(world, par2);
    }

    @WrapOperation(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;",
                    ordinal = 1
            )
    )
    private static Vec3 scale(Vec3 instance, double factor, Operation<Vec3> original) {
        return original.call(instance, factor+2/64f);
    }

    @ModifyVariable(
            method = "activateInner",
            at = @At("STORE"),
            ordinal = 2
    )
    private static Vec3 setRayOrigin(Vec3 value, @Local(argsOnly = true, ordinal = 0) DeployerFakePlayer player) {
        return VSGameUtilsKt.toWorldCoordinates(player.getLevel(), value);
    }

    @ModifyVariable(
            method = "activateInner",
            at = @At("STORE"),
            ordinal = 3
    )
    private static Vec3 setRayTarget(Vec3 value, @Local(argsOnly = true, ordinal = 0) DeployerFakePlayer player) {
        return VSGameUtilsKt.toWorldCoordinates(player.getLevel(), value);
    }

    @WrapOperation(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;clip(Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"
            )
    )
    private static BlockHitResult clip(ServerLevel instance, ClipContext clipContext, Operation<BlockHitResult> original) {
        return RaycastUtilsKt.clipIncludeShips(instance, clipContext, true);
    }

    @WrapOperation(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;"
            )
    )
    private static BlockPos redirectToTrue(BlockHitResult instance, Operation<BlockPos> original, @Local(argsOnly = true, ordinal = 0) BlockPos clickedPos) {
        return clickedPos;
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            index = 0
    )
    private static BlockPos replace1(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;mayInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace2(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace3(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace4(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace5(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace6(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace7(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace8(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;destroyBlockProgress(ILnet/minecraft/core/BlockPos;I)V",
                    ordinal = 0
            ),
            index = 1
    )
    private static BlockPos replace9(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;destroyBlockProgress(ILnet/minecraft/core/BlockPos;I)V",
                    ordinal = 1
            ),
            index = 1
    )
    private static BlockPos replace10(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static Object replace11(Object par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
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
    private static BlockPos replace12(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/deployer/DeployerHandler;safeOnUse(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
            ),
            index = 2
    )
    private static BlockPos replace13(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }

    @ModifyArg(
            method = "activateInner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseFireBlock;canBePlacedAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            ),
            index = 1
    )
    private static BlockPos replace14(BlockPos par1, @Local(ordinal = 0) BlockHitResult result) {
        return result.getBlockPos();
    }
}
