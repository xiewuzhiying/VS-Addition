package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity

class CheatCannonMountMethods : CannonMountMethods() {
    @LuaFunction(mainThread = true)
    fun setPitch(tileEntity: CannonMountBlockEntity, value: Double) {
        if (this.isRunning(tileEntity)) tileEntity.setPitch(value.toFloat())
    }

    @LuaFunction(mainThread = true)
    fun setYaw(tileEntity: CannonMountBlockEntity, value: Double) {
        if (this.isRunning(tileEntity)) tileEntity.setYaw(value.toFloat())
    }
}