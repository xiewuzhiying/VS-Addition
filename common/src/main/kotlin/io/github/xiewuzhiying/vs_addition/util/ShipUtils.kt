package io.github.xiewuzhiying.vs_addition.util

import io.github.xiewuzhiying.vs_addition.mixin.minecraft.HitResultAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ShipInertiaDataImplMixinDuck
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.impl.game.ships.ShipObjectClient
import org.valkyrienskies.core.util.expand
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.getShipsIntersecting
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.DimensionIdProvider
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft

object ShipUtils {
    @JvmStatic
    fun ServerLevel?.addMass(x: Double, y: Double, z: Double, mass: Double) {
        this.getShipManagingPos(x, y, z)?.addMass(x, y, z, mass)
    }

    @JvmStatic
    fun ServerShip?.addMass(x: Double, y: Double, z: Double, mass: Double) {
        (this?.inertiaData as? ShipInertiaDataImplMixinDuck)?.addMassAt(x, y, z, mass)
    }

    @JvmStatic
    fun Level?.getLoadedShipsIntersecting(aabb: AABBdc): Iterable<LoadedShip> {
        return this.shipObjectWorld.loadedShips.getIntersecting(aabb).filter { it.chunkClaimDimension == (this as DimensionIdProvider).dimensionId }
    }

    @JvmStatic
    fun Level?.getLoadedShipsIntersecting(aabb: AABB): Iterable<LoadedShip> {
        return this.shipObjectWorld.loadedShips.getIntersecting(aabb.toJOML())
    }

    //form VS base
    @JvmStatic
    @JvmOverloads
    fun Level.getPosStandingOnFromShips(blockPosInGlobal: Vector3dc, radius: Double = 0.5): BlockPos {
        val testAABB: AABBdc = AABBd(
            blockPosInGlobal.x(), blockPosInGlobal.y(), blockPosInGlobal.z() ,
            blockPosInGlobal.x(), blockPosInGlobal.y(), blockPosInGlobal.z()
        ).expand(radius)
        val intersectingShips = this.getShipsIntersecting(testAABB)
        intersectingShips.forEach { ship: Ship ->
            val blockPosInLocal: Vector3dc =
                ship.transform.worldToShip.transformPosition(blockPosInGlobal, Vector3d())
            val blockPos = blockPosInLocal.toBlockPos
            val blockState = this.getBlockState(blockPos)
            if (!blockState.isAir) {
                return blockPos
            } else {
                // Check the block below as well, in the cases of fences
                val blockPosInLocal2: Vector3dc = ship.transform.worldToShip
                    .transformPosition(Vector3d(blockPosInGlobal.x(), blockPosInGlobal.y() - 1.0, blockPosInGlobal.z()))
                val blockPos2 = blockPosInLocal2.toBlockPos
                val blockState2 = this.getBlockState(blockPos2)
                if (!blockState2.isAir) {
                    return blockPos2
                }
            }
        }
        val blockPos = blockPosInGlobal.toBlockPos
        val blockState = this.getBlockState(blockPos)
        return if (!blockState.isAir) {
            blockPos
        } else {
            Vector3d(blockPosInGlobal.x(), blockPosInGlobal.y() - 1.0, blockPosInGlobal.z()).toBlockPos
        }
    }

    @JvmStatic
    @JvmOverloads
    fun Level.clipIncludeShipsWrapper(ctx: ClipContext, clipFunction: (Level, ClipContext) -> HitResult = { _: Level, c: ClipContext -> vanillaClip(c) },
                                      shouldTransformHitPos: Boolean = true, skipShips: List<ShipId>? = null): HitResult {
        val originHit = clipFunction(this, ctx)

        if (shipObjectWorld == null) {
            return originHit
        }

        var closestHit = originHit
        var closestHitPos = originHit.location
        var closestHitDist = closestHitPos.distanceToSqr(ctx.from)

        val clipAABB: AABBdc = AABBd(ctx.from.toJOML(), ctx.to.toJOML()).correctBounds()

        // Iterate every ship, find do the raycast in ship space,
        // choose the raycast with the lowest distance to the start position.
        for (ship in shipObjectWorld.loadedShips.getIntersecting(clipAABB)) {
            // Skip skipShip
            if (skipShips != null && skipShips.contains(ship.id)) {
                continue
            }
            val worldToShip = (ship as? ShipObjectClient)?.renderTransform?.worldToShip ?: ship.worldToShip
            val shipToWorld = (ship as? ShipObjectClient)?.renderTransform?.shipToWorld ?: ship.shipToWorld

            ctx.setForm(worldToShip.transformPosition(ctx.from.toJOML()).toMinecraft())
            ctx.setTo(worldToShip.transformPosition(ctx.to.toJOML()).toMinecraft())

            val shipHit = clipFunction(this, ctx)
            val shipHitPos = shipToWorld.transformPosition(shipHit.location.toJOML()).toMinecraft()
            val shipHitDist = shipHit.location.distanceToSqr(ctx.from)

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

    fun BlockGetter.vanillaClip(context: ClipContext): BlockHitResult =
        BlockGetter.traverseBlocks(context.from, context.to, context,
            { clipContext: ClipContext, blockPos: BlockPos ->
                val blockState = getBlockState(blockPos)
                val fluidState = getFluidState(blockPos)
                val vec3 = clipContext.from
                val vec32 = clipContext.to
                val voxelShape = clipContext.getBlockShape(blockState, this, blockPos)
                val blockHitResult = clipWithInteractionOverride(vec3, vec32, blockPos, voxelShape, blockState)
                val voxelShape2 = clipContext.getFluidShape(fluidState, this, blockPos)
                val blockHitResult2 = voxelShape2.clip(vec3, vec32, blockPos)

                val d = if (blockHitResult == null)
                    Double.MAX_VALUE
                else
                    clipContext.from.distanceToSqr(blockHitResult.location)

                val e = if (blockHitResult2 == null)
                    Double.MAX_VALUE
                else
                    clipContext.from.distanceToSqr(blockHitResult2.location)

                if (d <= e)
                    blockHitResult
                else
                    blockHitResult2
            }, { ctx ->
                val vec3 = ctx.from.subtract(ctx.to)
                BlockHitResult.miss(
                    ctx.to, Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    BlockPos.containing(ctx.to)
                )
            })
}