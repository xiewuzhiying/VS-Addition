package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity

class CheatCannonMountPeripheral(type: String, tileEntity: CannonMountBlockEntity) : CannonMountPeripheral(type,
    tileEntity
) {
    @LuaFunction(mainThread = true)
    fun setPitch(value: Double) {
        if (this.isRunning()) tileEntity.setPitch(value.toFloat())
    }

    @LuaFunction(mainThread = true)
    fun setYaw(value: Double) {
        if (this.isRunning()) tileEntity.setYaw(value.toFloat())
    }
}