package io.github.xiewuzhiying.vs_addition.stuff

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.mod.common.entity.handling.VSEntityManager.getHandler
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import org.valkyrienskies.mod.common.util.toJOML

object EntityFreshCaller {
    fun freshEntityInShipyard(entity: Entity, serverLevel: ServerLevel) {
        val ship: LoadedShip? = serverLevel.getShipObjectManagingPos(entity.position().toJOML())
        if (ship != null) {
            getHandler(entity).freshEntityInShipyard(entity, ship)
        }
    }
}