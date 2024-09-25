package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.GenericPeripheral
import dan200.computercraft.api.peripheral.PeripheralType
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.mixin.createbigcannons.CannonMountBlockEntityAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.createbigcannons.MountedAutocannonContraptionMixinDuck
import net.minecraft.server.level.ServerLevel
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption


open class CannonMountMethods : GenericPeripheral {
    override fun id(): String {
        return "${VSAdditionMod.MOD_ID}:cbc_cannon_mount"
    }

    override fun getType(): PeripheralType {
        return PeripheralType.ofAdditional("cbc_cannon_mount")
    }

    @LuaFunction(mainThread = true)
    fun assemble(tileEntity: CannonMountBlockEntity): Any {
        if (!tileEntity.isRunning) {
            (tileEntity as CannonMountBlockEntityAccessor?)?.Assemble()
            (tileEntity.contraption?.contraption as AbstractMountedCannonContraption).onRedstoneUpdate(
                tileEntity.level as ServerLevel,
                tileEntity.contraption,
                false,
                0,
                tileEntity
            )
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun disassemble(tileEntity: CannonMountBlockEntity): Any {
        if (tileEntity.isRunning) {
            tileEntity.disassemble()
            tileEntity.sendData()
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun fire(tileEntity: CannonMountBlockEntity) {
        if (tileEntity.contraption?.level() is ServerLevel) {
            (tileEntity.contraption?.contraption as? MountedAutocannonContraptionMixinDuck)?.setIsCalledByComputer()
            (tileEntity.contraption?.contraption as AbstractMountedCannonContraption).fireShot(tileEntity.contraption?.level() as ServerLevel, tileEntity.contraption)
        }
    }

    @LuaFunction(mainThread = true)
    fun isRunning(tileEntity: CannonMountBlockEntity): Boolean {
        return tileEntity.isRunning
    }

    @LuaFunction
    fun getPitch(tileEntity: CannonMountBlockEntity): Double {
        return (tileEntity as CannonMountBlockEntityAccessor).cannonPitch.toDouble()
    }

    @LuaFunction
    fun getYaw(tileEntity: CannonMountBlockEntity): Double {
        return (tileEntity as CannonMountBlockEntityAccessor).cannonYaw.toDouble()
    }

    @LuaFunction
    fun getX(tileEntity: CannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.x
    }

    @LuaFunction
    fun getY(tileEntity: CannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.y
    }

    @LuaFunction
    fun getZ(tileEntity: CannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.z
    }

    @LuaFunction
    fun getMaxDepress(tileEntity: CannonMountBlockEntity): Double? {
        return tileEntity.contraption?.maximumDepression()?.toDouble()
    }

    @LuaFunction
    fun getMaxElevate(tileEntity: CannonMountBlockEntity): Double? {
        return tileEntity.contraption?.maximumElevation()?.toDouble()
    }
}