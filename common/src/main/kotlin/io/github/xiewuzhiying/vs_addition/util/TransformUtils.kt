package io.github.xiewuzhiying.vs_addition.util

import io.github.xiewuzhiying.vs_addition.mixin.minecraft.HitResultAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.minecraft.ClipContextMixinDuck
import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.EntityDraggingInformationMixinDuck
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import org.joml.*
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.impl.game.ships.ShipObjectClient
import org.valkyrienskies.core.util.expand
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.getShipsIntersecting
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.toWorldCoordinates
import org.valkyrienskies.mod.common.util.EntityDraggingInformation
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
    get() = Vec3i(this.x().toInt(), this.y().toInt(), this.z().toInt())

val Vector3fc.toVec3i : Vec3i
    get() = Vec3i(this.x().toInt(), this.y().toInt(), this.z().toInt())

val Vec3.toVec3i : Vec3i
    get() = Vec3i(this.x().toInt(), this.y().toInt(), this.z().toInt())

val Vector3dc.toBlockPos: BlockPos
    get() = BlockPos(this.x().toInt(), this.y().toInt(), this.z().toInt());

val Vector3fc.toBlockPos: BlockPos
    get() = BlockPos(this.x().toInt(), this.y().toInt(), this.z().toInt());

val Vec3.toBlockPos: BlockPos
    get() = BlockPos(this.x().toInt(), this.y().toInt(), this.z().toInt());

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

val Vector3i.centerMinecraft : Vec3
    get() = Vec3(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vector3i.centerJOMLD : Vector3d
    get() = Vector3d(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vector3i.centerJOMLF : Vector3f
    get() = Vector3f(this.x + 0.5f, this.y + 0.5f, this.z + 0.5f)

val Vec3i.centerMinecraft : Vec3
    get() = Vec3(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vec3i.centerJOMLD : Vector3d
    get() = Vector3d(this.x + 0.5, this.y + 0.5, this.z + 0.5)

val Vec3i.centerDJOMLF : Vector3f
    get() = Vector3f(this.x + 0.5f, this.y + 0.5f, this.z + 0.5f)

val Entity.isOnShip : Boolean
    get() = this.level().getShipsIntersecting(this.boundingBox).any()

val ClipContext.block : ClipContext.Block
    get() = (this as ClipContextMixinDuck).block

val ClipContext.fluid : ClipContext.Fluid
    get() = (this as ClipContextMixinDuck).fluid

val ClipContext.entity : Entity
    get() = (this as ClipContextMixinDuck).entity

val ClipContext.collisionContext : CollisionContext
    get() = (this as ClipContextMixinDuck).collisionContext

fun ClipContext.setForm(vec3: Vec3) {
    (this as ClipContextMixinDuck).setForm(vec3)
}

fun ClipContext.setTo(vec3: Vec3) {
    (this as ClipContextMixinDuck).setTo(vec3)
}

fun ClipContext.setBlock(block: ClipContext.Block) {
    (this as ClipContextMixinDuck).block = block
}

fun ClipContext.setFluid(fluid: ClipContext.Fluid) {
    (this as ClipContextMixinDuck).fluid = fluid
}

fun ClipContext.setEntity(entity: Entity) {
    (this as ClipContextMixinDuck).entity = entity
}

fun ClipContext.setCollisionContext(ctx: CollisionContext) {
    (this as ClipContextMixinDuck).collisionContext = ctx
}

var EntityDraggingInformation.addedPitchRotLastTick : Double
    get() = (this as EntityDraggingInformationMixinDuck).addedPitchRotLastTick
    set(value) { (this as EntityDraggingInformationMixinDuck).addedPitchRotLastTick = value }

data class EntityHit(val entity: Entity, val vec3: Vec3)

@JvmOverloads
fun Level.clipEntities(start: Vec3, end: Vec3, aabb: AABB, skipEntities: List<Entity>? = null): EntityHit? {
    var closestEntity: Entity? = null
    var closestVec3: Vec3 = end
    var closestDis: Double = start.distanceToSqr(end)
    this.getEntities(null, aabb).filter { skipEntities == null || !skipEntities.contains(it) }.forEach {
        val entityAABB = it.boundingBox
        if (it is ItemEntity) {
            entityAABB.inflate(0.75)
        }
        val hitVec3 = entityAABB.clip(start, end)
        if (hitVec3.isPresent && closestDis < start.distanceToSqr(hitVec3.get())) {
            closestEntity = it
            closestVec3 = hitVec3.get()
        }
    }
    return closestEntity?.let { EntityHit(it, closestVec3) }
}