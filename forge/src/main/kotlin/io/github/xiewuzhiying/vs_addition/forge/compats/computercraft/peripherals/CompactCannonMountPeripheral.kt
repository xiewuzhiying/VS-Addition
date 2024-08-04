package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare.CompactCannonMountBlockEntityAccessor
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity

open class CompactCannonMountPeripheral(
    val peripheralType: String,
    val tileEntity: CompactCannonMountBlockEntity
) : IPeripheral {
    override fun getType(): String {
        return peripheralType
    }

    override fun equals(iPeripheral: IPeripheral?): Boolean {
        return iPeripheral === this
    }

    override fun getTarget(): Any? {
        return this.tileEntity
    }

    @LuaFunction(mainThread = true)
    fun assemble(): Any {
        if (!tileEntity.isRunning) {
            (tileEntity as CompactCannonMountBlockEntityAccessor?)!!.Assemble()
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun disassemble(): Any {
        if (tileEntity.isRunning) {
            tileEntity.disassemble()
            tileEntity.sendData()
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun fire() {
        if (tileEntity.isRunning) {
            tileEntity.contraption!!.tryFiringShot()
        }
    }

    @LuaFunction(mainThread = true)
    fun isRunning(): Boolean {
        return tileEntity.isRunning
    }

    @LuaFunction
    fun getPitch(): Double {
        return (tileEntity as CompactCannonMountBlockEntityAccessor).cannonPitch.toDouble()
    }

    @LuaFunction
    fun getYaw(): Double {
        return (tileEntity as CompactCannonMountBlockEntityAccessor).cannonYaw.toDouble()
    }

    @LuaFunction
    fun getMaxDepress(): Any {
        return tileEntity.contraption!!.maximumDepression().toDouble()
    }

    @LuaFunction
    fun getMaxElevate(): Any {
        return tileEntity.contraption!!.maximumElevation().toDouble()
    }
}