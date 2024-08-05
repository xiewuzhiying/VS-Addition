package io.github.xiewuzhiying.vs_addition.util

import com.simibubi.create.AllTags
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.HitResultAccessor
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipBlockStateContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.apache.logging.log4j.LogManager
import org.joml.Matrix4dc
import org.joml.primitives.AABBd
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.mod.common.getShipsIntersecting
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft

object RaycastUtils {
    @JvmStatic
    fun clipIncludeShips(level: Level, ctx: ClipContext): BlockHitResult {
        val vanillaHit = clip(level, ctx, ctx.from, ctx.to)
        val shipObjectWorld = level.shipObjectWorld
        var closestHit = vanillaHit
        var closestHitPos = vanillaHit.location
        var closestHitDist = closestHitPos.distanceToSqr(ctx.from)
        val clipAABB = AABBd(ctx.from.toJOML(), ctx.to.toJOML()).correctBounds()
        for (ship in shipObjectWorld.loadedShips.getIntersecting(clipAABB)) {
            val worldToShip: Matrix4dc
            val shipToWorld: Matrix4dc
            if (ship is ClientShip) {
                worldToShip = ship.renderTransform?.worldToShip ?: ship.worldToShip
                shipToWorld = ship.renderTransform?.shipToWorld ?: ship.shipToWorld
            } else {
                worldToShip = ship.worldToShip
                shipToWorld = ship.shipToWorld
            }
            val shipStart = worldToShip.transformPosition(ctx.from.toJOML()).toMinecraft()
            val shipEnd = worldToShip.transformPosition(ctx.to.toJOML()).toMinecraft()
            val shipHit = clip(level, ctx, shipStart, shipEnd)
            val shipHitPos = shipToWorld.transformPosition(shipHit.location.toJOML()).toMinecraft()
            val shipHitDist = shipHitPos.distanceToSqr(ctx.from)
            if (shipHitDist < closestHitDist && shipHit.type != HitResult.Type.MISS) {
                closestHit = shipHit
                closestHitPos = shipHitPos
                closestHitDist = shipHitDist
            }
        }
        return BlockHitResult(closestHitPos, closestHit.direction, closestHit.blockPos, closestHit.isInside)
    }

    private fun clip(level: Level, context: ClipContext, realStart: Vec3, realEnd: Vec3): BlockHitResult {
        return BlockGetter.traverseBlocks(realStart, realEnd, context,
            { context1: ClipContext, blockPos: BlockPos? ->
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
                    BlockPos.containing(end)
                )
            })
    }

    @JvmStatic
    fun isBlockInLineIncludeShips(level: Level, clipBlockStateContext: ClipBlockStateContext): BlockHitResult {
        return isBlockInLineIncludeShips(level, clipBlockStateContext, true)
    }

    @JvmStatic
    fun isBlockInLineIncludeShips(level: Level, clipBlockStateContext: ClipBlockStateContext, shouldTransformHitPos: Boolean): BlockHitResult {
        return isBlockInLineIncludeShips(level, clipBlockStateContext, shouldTransformHitPos, null)
    }

    @JvmStatic
    fun isBlockInLineIncludeShips(level: Level, clipBlockStateContext: ClipBlockStateContext, shouldTransformHitPos: Boolean, skipShip: Long?): BlockHitResult {
        val vanillaHit = isBlockInLine(level, clipBlockStateContext)
        val shipObjectWorld = level.shipObjectWorld
        if (shipObjectWorld == null) {
            val logger = LogManager.getLogger("RaycastUtilsKt")
            logger.error("shipObjectWorld was empty for level raytrace, this should not be possible! Returning vanilla result.")
            return vanillaHit
        }
        var closestHit = vanillaHit
        var closestHitPos = vanillaHit.location
        var closestHitDist = closestHitPos.distanceToSqr(clipBlockStateContext.from)
        val clipAABB = AABBd(clipBlockStateContext.from.toJOML(), clipBlockStateContext.to.toJOML()).correctBounds()
        for (ship in level.getShipsIntersecting(clipAABB)) {
            if (skipShip == ship.id) {
                continue
            }
            val worldToShip: Matrix4dc
            val shipToWorld: Matrix4dc
            if (ship is ClientShip) {
                worldToShip = ship.renderTransform?.worldToShip ?: ship.worldToShip
                shipToWorld = ship.renderTransform?.shipToWorld ?: ship.shipToWorld
            } else {
                worldToShip = ship.worldToShip
                shipToWorld = ship.shipToWorld
            }
            val shipStart = worldToShip.transformPosition(clipBlockStateContext.from.toJOML()).toMinecraft()
            val shipEnd = worldToShip.transformPosition(clipBlockStateContext.to.toJOML()).toMinecraft()
            val shipHit = isBlockInLine(level, ClipBlockStateContext(shipStart, shipEnd, clipBlockStateContext.isTargetBlock))
            val shipHitPos = shipToWorld.transformPosition(shipHit.location.toJOML()).toMinecraft()
            val shipHitDist = shipHitPos.distanceToSqr(clipBlockStateContext.from)
            if (shipHitDist < closestHitDist && shipHit.type != HitResult.Type.MISS) {
                closestHit = shipHit
                closestHitPos = shipHitPos
                closestHitDist = shipHitDist
            }
        }
        if (shouldTransformHitPos) {
            (closestHit as HitResultAccessor).setLocation(closestHitPos)
        }
        return closestHit
    }

    private fun isBlockInLine(blockGetter: BlockGetter, arg: ClipBlockStateContext): BlockHitResult {
        return BlockGetter.traverseBlocks(arg.from, arg.to, arg,
            { argx: ClipBlockStateContext, arg2: BlockPos? ->
                val blockstate = blockGetter.getBlockState(arg2)
                val vec3 = argx.from.subtract(argx.to)
                if (argx.isTargetBlock().test(blockstate)) BlockHitResult(
                    argx.to,
                    Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    BlockPos.containing(argx.to),
                    false
                ) else null
            },
            { argx: ClipBlockStateContext ->
                val vec3 = argx.from.subtract(argx.to)
                BlockHitResult.miss(
                    argx.to,
                    Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    BlockPos.containing(argx.to)
                )
            }) as BlockHitResult
    }
}
