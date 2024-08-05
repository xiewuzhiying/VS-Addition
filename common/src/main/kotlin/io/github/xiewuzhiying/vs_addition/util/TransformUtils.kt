package io.github.xiewuzhiying.vs_addition.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.*
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.getShipManagingPos
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

val Vector3d.toVec3i : Vec3i
    get() = Vec3i(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())

val Vector3f.toVec3i : Vec3i
    get() = Vec3i(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())

val Vec3.toVec3i : Vec3i
    get() = Vec3i(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())

val Vector3d.toBlockPos: BlockPos
    get() = this.toVec3i as BlockPos;

val Vector3f.toBlockPos: BlockPos
    get() = this.toVec3i as BlockPos;

val Vec3.toBlockPos: BlockPos
    get() = this.toVec3i as BlockPos;

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

fun getCenterOf(vec3i: Vec3i): Vec3 {
    return Vec3(vec3i.x + 0.5, vec3i.y + 0.5, vec3i.z + 0.5)
}