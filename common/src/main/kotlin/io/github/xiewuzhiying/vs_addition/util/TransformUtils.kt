package io.github.xiewuzhiying.vs_addition.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import org.joml.*
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.getShipsIntersecting
import org.valkyrienskies.mod.common.toWorldCoordinates
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft
import java.lang.Math
import kotlin.math.floor

val Direction.directionToQuaterniond : Quaterniond
    get() =
        when (this) {
            Direction.UP -> Quaterniond()
            Direction.DOWN -> Quaterniond(AxisAngle4d(Math.PI, Vector3d(1.0, 0.0, 0.0)))
            Direction.EAST -> Quaterniond(AxisAngle4d(0.5 * Math.PI, Vector3d(0.0, 1.0, 0.0))).mul(
                Quaterniond(
                    AxisAngle4d(
                        Math.PI / 2.0, Vector3d(1.0, 0.0, 0.0)
                    )
                )
            ).normalize()

            Direction.WEST -> Quaterniond(AxisAngle4d(1.5 * Math.PI, Vector3d(0.0, 1.0, 0.0))).mul(
                Quaterniond(
                    AxisAngle4d(
                        Math.PI / 2.0, Vector3d(1.0, 0.0, 0.0)
                    )
                )
            ).normalize()

            Direction.SOUTH -> Quaterniond(AxisAngle4d(Math.PI / 2.0, Vector3d(1.0, 0.0, 0.0))).normalize()
            Direction.NORTH -> Quaterniond(AxisAngle4d(Math.PI, Vector3d(0.0, 1.0, 0.0))).mul(
                Quaterniond(
                    AxisAngle4d(
                        Math.PI / 2.0, Vector3d(1.0, 0.0, 0.0)
                    )
                )
            ).normalize()
        }

val Direction.directionToQuaternionf : Quaternionf
    get() = Quaternionf(this.directionToQuaterniond)

fun Vec3.toShipyardCoordinates(ship: Ship): Vec3 {
    val vector3d = ship.worldToShip.transformPosition(this.toJOML())
    return vector3d.toMinecraft()
}

val Vector3dc.toVec3i : Vec3i
    get() = Vec3i(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt())

val Vector3fc.toVec3i : Vec3i
    get() = Vec3i(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt())

val Vec3.toVec3i : Vec3i
    get() = Vec3i(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt())

val Vector3dc.toBlockPos: BlockPos
    get() = BlockPos(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt());

val Vector3fc.toBlockPos: BlockPos
    get() = BlockPos(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt());

val Vec3.toBlockPos: BlockPos
    get() = BlockPos(floor(this.x()).toInt(), floor(this.y()).toInt(), floor(this.z()).toInt());

val Vec3i.toVec3: Vec3
    get() = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble());

fun Vec3.toWorld(level: Level): Vec3 {
    val ship = level.getShipManagingPos(this)
    if (ship != null) {
        return ship.toWorldCoordinates(this)
    }
    return this
}

fun Vec3.toWorld(ship: Ship?): Vec3 {
    if (ship != null) {
        return ship.toWorldCoordinates(this)
    }
    return this
}

fun Vec3.below(distance: Double): Vec3 {
    val direction = Direction.DOWN
    return Vec3(
        this.x + direction.stepX * distance,
        this.y + direction.stepY * distance,
        this.z + direction.stepZ * distance
    )
}

fun BlockPos.front(direction: Direction): Vec3 {
    return when (direction) {
        Direction.EAST -> Vec3(this.x + 1.0, this.y + 0.5, this.z + 0.5)
        Direction.SOUTH -> Vec3(this.x + 0.5, this.y + 0.5, this.z + 1.0)
        Direction.WEST -> Vec3(this.x.toDouble(), this.y + 0.5, this.z + 0.5)
        Direction.NORTH -> Vec3(this.x + 0.5, this.y + 0.5, this.z.toDouble())
        Direction.UP -> Vec3(this.x + 0.5, this.y + 1.0, this.z + 0.5)
        else -> Vec3(this.x + 0.5, this.y.toDouble(), this.z + 0.5)
    }
}

val Vector3i.centerM : Vec3
    get() = Vec3(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vector3i.centerD : Vector3d
    get() = Vector3d(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vector3i.centerDF : Vector3f
    get() = Vector3f(this.x + 0.5f, this.y + 0.5f, this.z + 0.5f)

val Vec3i.centerM : Vec3
    get() = Vec3(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vec3i.centerD : Vector3d
    get() = Vector3d(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vec3i.centerDF : Vector3f
    get() = Vector3f(this.x + 0.5f, this.y + 0.5f, this.z + 0.5f)

val Entity.isOnShip : Boolean
    get() = this.level().getShipsIntersecting(this.boundingBox).any()

//form VS base
fun Level.getPosStandingOnFromShips(blockPosInGlobal: Vector3dc, radius: Double): BlockPos {
    val testAABB: AABBdc = AABBd(
        blockPosInGlobal.x() - radius, blockPosInGlobal.y() - radius, blockPosInGlobal.z() - radius,
        blockPosInGlobal.x() + radius, blockPosInGlobal.y() + radius, blockPosInGlobal.z() + radius
    )
    val intersectingShips = this.getShipsIntersecting(testAABB)
    for (ship in intersectingShips) {
        val blockPosInLocal: Vector3dc =
            ship.transform.worldToShip.transformPosition(blockPosInGlobal, Vector3d())
        val blockPos = blockPosInLocal.toBlockPos
        val blockState = this.getBlockState(blockPos)
        if (blockState != Blocks.AIR.defaultBlockState()) {
            return blockPos
        } else {
            // Check the block below as well, in the cases of fences
            val blockPosInLocal2: Vector3dc = ship.transform.worldToShip
                .transformPosition(
                    Vector3d(blockPosInGlobal.x(), blockPosInGlobal.y() - 1.0, blockPosInGlobal.z())
                )
            val blockPos2 = blockPosInLocal2.toBlockPos
            val blockState2 = this.getBlockState(blockPos2)
            if (!blockState2.isAir) {
                return blockPos2
            }
        }
    }
    return blockPosInGlobal.toBlockPos
}