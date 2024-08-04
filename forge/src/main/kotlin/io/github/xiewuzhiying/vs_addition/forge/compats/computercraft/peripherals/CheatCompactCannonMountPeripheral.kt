package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals

import dan200.computercraft.api.lua.LuaFunction
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity

class CheatCompactCannonMountPeripheral(type: String, tileEntity: CompactCannonMountBlockEntity
) : CompactCannonMountPeripheral(type, tileEntity) {
    @LuaFunction(mainThread = true)
    fun setPitch(value: Double) {
        if (this.isRunning()) tileEntity.setPitch(value.toFloat())
    }

    @LuaFunction(mainThread = true)
    fun setYaw(value: Double) {
        if (this.isRunning()) tileEntity.setYaw(value.toFloat())
    }
}