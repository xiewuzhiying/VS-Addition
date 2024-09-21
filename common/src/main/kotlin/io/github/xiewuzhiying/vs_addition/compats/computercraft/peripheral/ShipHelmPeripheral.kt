package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.eureka.EurekaConfig
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity
import org.valkyrienskies.eureka.ship.EurekaShipControl
import org.valkyrienskies.eureka.util.ShipAssembler.collectBlocks
import org.valkyrienskies.mod.api.SeatedControllingPlayer
import org.valkyrienskies.mod.common.getShipManagingPos
import kotlin.math.max
import kotlin.math.min

class ShipHelmPeripheral(
    val peripheralType: String,
    val tileEntity: ShipHelmBlockEntity,
    val level: Level,
    val blockPos: BlockPos
) : IPeripheral {
    override fun getType(): String {
        return peripheralType
    }

    override fun equals(p0: IPeripheral?): Boolean {
        return p0 === this
    }

    override fun getTarget(): Any {
        return this.tileEntity
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun disassemble() {
        if (tileEntity.assembled) {
            tileEntity.disassemble()
        } else throw LuaException("Not assembled yet")
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun assemble() {
        if (!tileEntity.assembled) {
            val builtShip = collectBlocks(
                (level as ServerLevel?)!!,
                blockPos
            ) { blockState: BlockState ->
                !blockState.isAir && !EurekaConfig.SERVER.blockBlacklist.contains(
                    Registry.BLOCK.getKey(blockState.block).toString()
                )
            }
            if (builtShip == null) throw LuaException("Ship is too big! Max size is" + EurekaConfig.SERVER.maxShipBlocks + "blocks (changable in the config)")
        } else throw LuaException("Already assembled")
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun align() {
        if (tileEntity.assembled) {
            tileEntity.align()
        } else throw LuaException("Not assembled yet")
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun move(args: IArguments) {
        if (level.isClientSide()) throw LuaException("client")
        val ship = level.getShipManagingPos(blockPos) as ServerShip? ?: throw LuaException("No ship")
        if (ship.getAttachment(EurekaShipControl::class.java) == null) throw LuaException("Not Eureka ship")
        var fakePlayer = ship.getAttachment(SeatedControllingPlayer::class.java)
        val direction = level.getBlockState(this.blockPos).getValue(BlockStateProperties.HORIZONTAL_FACING).opposite
        if (fakePlayer == null || fakePlayer.seatInDirection != direction) {
            fakePlayer = SeatedControllingPlayer(direction)
            ship.saveAttachment(SeatedControllingPlayer::class.java, fakePlayer)
        }
        fakePlayer.leftImpulse = min(max(args.getDouble(0), -1.0), 1.0).toFloat()
        fakePlayer.upImpulse = min(max(args.getDouble(1), -1.0), 1.0).toFloat()
        fakePlayer.forwardImpulse = min(max(args.getDouble(2), -1.0), 1.0).toFloat()
    }

    @LuaFunction
    fun fakePlayerKill() {
        if (level.isClientSide()) throw LuaException("client")
        val ship = level.getShipManagingPos(blockPos) as ServerShip? ?: throw LuaException("No ship")
        ship.saveAttachment(SeatedControllingPlayer::class.java, null)
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun getBalloonAmount(): Int {
        return check().balloons
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun getAnchorAmount(): Int {
        return check().anchors
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun getActiveAnchorAmount(): Int {
        return check().anchorsActive
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun areAnchorsActive(): Boolean {
        return check().anchorsActive > 0
    }

    @LuaFunction
    @Throws(LuaException::class)
    fun getShipHelmAmount(): Int {
        return check().helms
    }
    
    private fun check() : EurekaShipControl {
        if (level.isClientSide()) throw LuaException("client")
        val ship = level.getShipManagingPos(blockPos) as ServerShip? ?: throw LuaException("No ship")
        val control = ship.getAttachment(EurekaShipControl::class.java) ?: throw LuaException("Not Eureka ship")
        return control;
    }
}