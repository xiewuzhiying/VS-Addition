package io.github.xiewuzhiying.vs_addition.compats.create.content.kinetics.fan

import com.simibubi.create.AllTags
import io.github.xiewuzhiying.vs_addition.util.toBlockPos
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipBlockStateContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

object AirCurrentUtils {
    @JvmStatic
    @JvmOverloads
    fun clip(level: Level, context: ClipContext, realStart: Vec3 = context.from, realEnd: Vec3 = context.to): BlockHitResult {
        return BlockGetter.traverseBlocks(realStart, realEnd, context,
            { context1: ClipContext, blockPos: BlockPos ->
                val blockState = level.getBlockState(blockPos)
                if (AllTags.AllBlockTags.FAN_TRANSPARENT.matches(blockState)) {
                    return@traverseBlocks null
                }
                val var17 = level.getFluidState(blockPos)
                val voxelShape =
                    context1.getBlockShape(blockState, level, blockPos)
                val blockHitResult =
                    level.clipWithInteractionOverride(realStart, realEnd, blockPos, voxelShape, blockState)
                val voxelShape2 = context.getFluidShape(var17, level, blockPos)
                val blockHitResult2 =
                    voxelShape2.clip(realStart, realEnd, blockPos)
                val d =
                    if (blockHitResult == null) Double.MAX_VALUE else realStart.distanceToSqr(
                        blockHitResult.location
                    )
                val e =
                    if (blockHitResult2 == null) Double.MAX_VALUE else realEnd.distanceToSqr(
                        blockHitResult2.location
                    )
                if (d <= e) blockHitResult else blockHitResult2
            },
            { context1: ClipContext ->
                val start = context1.from
                val end = context1.to
                val vec3d = start.subtract(end)
                BlockHitResult.miss(
                    end,
                    Direction.getNearest(vec3d.x, vec3d.y, vec3d.z),
                    end.toBlockPos
                )
            })
    }

    @JvmStatic
    fun isBlockInLine(blockGetter: BlockGetter, arg: ClipBlockStateContext): BlockHitResult {
        return BlockGetter.traverseBlocks(arg.from, arg.to, arg,
            { argx: ClipBlockStateContext, arg2: BlockPos ->
                val blockState = blockGetter.getBlockState(arg2)
                val vec3 = argx.from.subtract(argx.to)
                if (argx.isTargetBlock().test(blockState)) BlockHitResult(
                    argx.to,
                    Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    argx.to.toBlockPos,
                    false
                ) else null
            },
            { argx: ClipBlockStateContext ->
                val vec3 = argx.from.subtract(argx.to)
                BlockHitResult.miss(
                    argx.to,
                    Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    argx.to.toBlockPos
                )
            }) as BlockHitResult
    }
}