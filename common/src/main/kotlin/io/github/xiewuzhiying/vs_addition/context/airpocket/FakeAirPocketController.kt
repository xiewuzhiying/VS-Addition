package io.github.xiewuzhiying.vs_addition.context.airpocket

import com.fasterxml.jackson.annotation.JsonAutoDetect
import io.github.xiewuzhiying.vs_addition.PlatformUtils
import net.minecraft.server.level.ServerLevel
import org.joml.primitives.AABBdc
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.apigame.world.properties.DimensionId
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getLevelFromDimensionId
import org.valkyrienskies.mod.common.shipObjectWorld
import java.util.*

typealias PocketId = Long

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
class FakeAirPocketController {
    var nextId : PocketId = 0
    var level : DimensionId = "minecraft:overworld"
    val map : MutableMap<Long, AABBdc> = mutableMapOf()

    fun addAirPocket(aabb: AABBdc) : PocketId {
        val id = nextId++
        this.map[id] = aabb
        return id
    }

    fun getAirPocket(pocketId: PocketId) : AABBdc? {
        return this.map[pocketId]
    }

    fun getAllAirPocket() : Map<PocketId, AABBdc> {
        return this.map.toMutableMap()
    }

    fun removeAirPocket(pocketId: PocketId) {
        this.map.remove(pocketId)
    }

    fun removeAllAirPocket() {
        this.map.clear()
    }

    fun setAirPocket(pocketId: PocketId, aabb: AABBdc) {
        this.map[pocketId] = aabb
    }

    companion object{
        fun getOrCreate(ship: ServerShip, level: DimensionId) =
            ship.getAttachment<FakeAirPocketController>()
                ?: FakeAirPocketController().also {
                    it.level = level
                    ship.saveAttachment(it)
                }
        fun getOrCreate(shipId: ShipId, level: DimensionId) =
            PlatformUtils.getMinecraftServer().getLevelFromDimensionId(level).shipObjectWorld.allShips.getById(shipId)?.let { ship ->
                ship.getAttachment<FakeAirPocketController>() ?: FakeAirPocketController().also {
                    it.level = level
                    ship.saveAttachment(it)
                }
            }
        fun getOrCreate(ship: ServerShip, level: ServerLevel) =
            ship.getAttachment<FakeAirPocketController>()
                ?: FakeAirPocketController().also {
                    it.level = level.dimensionId
                    ship.saveAttachment(it)
                }
        fun getOrCreate(shipId: ShipId, level: ServerLevel) =
            level.shipObjectWorld.allShips.getById(shipId)?.let { ship ->
                ship.getAttachment<FakeAirPocketController>() ?: FakeAirPocketController().also {
                    it.level = level.dimensionId
                    ship.saveAttachment(it)
                }
            }
    }
}