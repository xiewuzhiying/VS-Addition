package io.github.xiewuzhiying.vs_addition.compats.create.content.decoration.steamWhistle

import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock
import com.simibubi.create.content.decoration.steamWhistle.WhistleSoundInstance
import net.minecraft.core.BlockPos
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.client.audio.VelocityTickableSoundInstance

class WhistleSoundInstanceOnShip(size: WhistleBlock.WhistleSize, worldPosition: BlockPos, private val ship: Ship) : WhistleSoundInstance(
    size, worldPosition), VelocityTickableSoundInstance {
    private val originalPos = Vector3d(x, y, z)

    override val velocity: Vector3dc
        get() = ship.velocity

    override fun tick() {
        val newPos = ship.shipToWorld.transformPosition(originalPos, Vector3d())
        this.x = newPos.x
        this.y = newPos.y
        this.z = newPos.z
        super.tick()
    }
}