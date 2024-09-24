package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import io.github.xiewuzhiying.vs_addition.mixinducks.vs_clockwork.flap_bearing.FlapBearingBlockEntityMixinDuck
import org.valkyrienskies.clockwork.content.contraptions.flap.FlapBearingBlockEntity

class CheatFlapBearingPeripheral(peripheralType: String, tileEntity: FlapBearingBlockEntity) : FlapBearingPeripheral(peripheralType,
    tileEntity
) {
    @LuaFunction(mainThread = true)
    override fun setAngle(angle: Double): Boolean {
        if (tileEntity.isRunning) {
            (this.tileEntity as FlapBearingBlockEntityMixinDuck).lockedFlapAngle = angle.toFloat()
            tileEntity.setAngle(angle.toFloat())
            (this.tileEntity as FlapBearingBlockEntityMixinDuck).isLocked = true
            return true
        }
        return false
    }
}