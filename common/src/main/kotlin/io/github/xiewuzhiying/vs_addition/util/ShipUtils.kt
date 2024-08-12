package io.github.xiewuzhiying.vs_addition.util

import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ShipInertiaDataImplMixinDuck
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.mod.common.getShipManagingPos

object ShipUtils {
    fun ServerLevel?.addMass(x: Double, y: Double, z: Double, mass: Double) {
        this.getShipManagingPos(x, y, z)?.addMass(x, y, z, mass)
    }

    fun ServerShip?.addMass(x: Double, y: Double, z: Double, mass: Double) {
        (this?.inertiaData as? ShipInertiaDataImplMixinDuck)?.addMassAt(x, y, z, mass)
    }
}