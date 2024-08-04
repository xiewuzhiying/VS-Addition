package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity

class CheatFlapBearingPeripheral(type: String, tileEntity: FlapBearingBlockEntity) : FlapBearingPeripheral(type,
    tileEntity
) {
    @LuaFunction(mainThread = true)
    fun setAngle(angle: Double): Any {
        if (tileEntity.isRunning) {
            tileEntity.setAngle(angle.toFloat())
            return true
        }
        return false
    }
}