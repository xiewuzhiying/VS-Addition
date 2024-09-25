package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals

import dan200.computercraft.api.lua.LuaFunction
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity

class CheatCompactCannonMountMethods : CompactCannonMountMethods() {
    @LuaFunction(mainThread = true)
    fun setPitch(tileEntity: CompactCannonMountBlockEntity, value: Double) {
        if (this.isRunning(tileEntity)) tileEntity.setPitch(value.toFloat())
    }

    @LuaFunction(mainThread = true)
    fun setYaw(tileEntity: CompactCannonMountBlockEntity, value: Double) {
        if (this.isRunning(tileEntity)) tileEntity.setYaw(value.toFloat())
    }
}