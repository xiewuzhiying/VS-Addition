package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.flap_bearing.FlapBearingBlockEntityAccessor
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity

open class FlapBearingPeripheral(val type: String, val tileEntity: FlapBearingBlockEntity) : IPeripheral {
    override fun getType(): String {
        return type
    }

    override fun equals(iPeripheral: IPeripheral?): Boolean {
        return iPeripheral === this
    }

    override fun getTarget(): Any {
        return this.tileEntity
    }

    @LuaFunction(mainThread = true)
    fun assemble(): Any {
        if (!tileEntity.isRunning) {
            tileEntity.assemble()
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun disassemble(): Boolean {
        if (tileEntity.isRunning) {
            tileEntity.disassemble()
            return true
        }
        return false
    }

    @LuaFunction
    fun getAngle(): Any {
        return (tileEntity as FlapBearingBlockEntityAccessor).bearingAngle
    }
}