package io.github.xiewuzhiying.vs_addition.forge.compats.netmusic

import com.github.tartaricacid.netmusic.client.audio.NetMusicSound
import net.minecraft.core.BlockPos
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.client.audio.VelocityTickableSoundInstance
import java.net.URL

class NetMusicSoundOnShip(pos: BlockPos, songUrl: URL, timeSecond: Int, private val ship: Ship) : NetMusicSound(
    pos,
    songUrl,
    timeSecond
), VelocityTickableSoundInstance {

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