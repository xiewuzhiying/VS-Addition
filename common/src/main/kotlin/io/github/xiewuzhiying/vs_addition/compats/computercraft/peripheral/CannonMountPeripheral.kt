package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral
import io.github.xiewuzhiying.vs_addition.mixin.createbigcannons.CannonMountBlockEntityAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.createbigcannons.MountedAutocannonContraptionMixinDuck
import net.minecraft.server.level.ServerLevel
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption

open class CannonMountPeripheral(
    val peripheralType: String,
    val tileEntity: CannonMountBlockEntity
) : IPeripheral{
    override fun getType(): String {
        return peripheralType
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
            (tileEntity as CannonMountBlockEntityAccessor?)?.Assemble()
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
        if (this.tileEntity.contraption?.level() is ServerLevel) {
            (this.tileEntity.contraption?.contraption as? MountedAutocannonContraptionMixinDuck)?.setIsCalledByComputer()
            (this.tileEntity.contraption?.contraption as AbstractMountedCannonContraption).fireShot(tileEntity.contraption?.level() as ServerLevel, this.tileEntity.contraption)
        }
    }

    @LuaFunction(mainThread = true)
    fun isRunning(): Boolean {
        return tileEntity.isRunning
    }

    @LuaFunction
    fun getPitch(): Double {
        return (tileEntity as CannonMountBlockEntityAccessor).cannonPitch.toDouble()
    }

    @LuaFunction
    fun getYaw(): Double {
        return (tileEntity as CannonMountBlockEntityAccessor).cannonYaw.toDouble()
    }

    @LuaFunction
    fun getX(): Int {
        return tileEntity.controllerBlockPos.x
    }

    @LuaFunction
    fun getY(): Int {
        return tileEntity.controllerBlockPos.y
    }

    @LuaFunction
    fun getZ(): Int {
        return tileEntity.controllerBlockPos.z
    }

    @LuaFunction
    fun getMaxDepress(): Double? {
        return tileEntity.contraption?.maximumDepression()?.toDouble()
    }

    @LuaFunction
    fun getMaxElevate(): Double? {
        return tileEntity.contraption?.maximumElevation()?.toDouble()
    }
}