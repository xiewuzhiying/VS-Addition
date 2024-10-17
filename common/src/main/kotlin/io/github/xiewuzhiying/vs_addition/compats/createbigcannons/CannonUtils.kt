package io.github.xiewuzhiying.vs_addition.compats.createbigcannons

import com.simibubi.create.content.contraptions.OrientedContraptionEntity
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.EntityAccessor
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import org.valkyrienskies.mod.common.util.GameTickForceApplier
import org.valkyrienskies.mod.common.util.toJOML
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile

object CannonUtils {
    @JvmStatic
    fun recoil(
        instance: AbstractCannonProjectile,
        x: Double, y: Double, z: Double,
        velocity: Float, inaccuracy: Float,
        entity: OrientedContraptionEntity,
        force: Double,
        originalFunction: (AbstractCannonProjectile, Double, Double, Double, Float, Float) -> Void) {
        val ship = entity.level().getShipObjectManagingPos(entity.anchorVec.toJOML())
        if (ship != null) {
            val vec3 = (Vec3(x, y, z)).normalize().add(
                (instance as EntityAccessor).random.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble() * VSAdditionConfig.SERVER.createBigCannons.spreadMultiplier,
                (instance as EntityAccessor).random.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble() * VSAdditionConfig.SERVER.createBigCannons.spreadMultiplier,
                (instance as EntityAccessor).random.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble() * VSAdditionConfig.SERVER.createBigCannons.spreadMultiplier
            ).scale(velocity.toDouble())
            val applier = (ship as ServerShip).getAttachment(GameTickForceApplier::class.java)
            val recoilForce: Double = velocity * force
            applier!!.applyInvariantForceToPos(
                ship.transform.shipToWorldRotation.transform(
                    vec3.toJOML().negate().normalize()
                ).mul(recoilForce), entity.anchorVec.add(0.5, 0.5, 0.5).toJOML().sub(ship.transform.positionInShip)
            )
        }
        originalFunction(instance, x, y, z, velocity, inaccuracy)
    }
}