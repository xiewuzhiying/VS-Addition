package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.GenericPeripheral
import dan200.computercraft.api.peripheral.PeripheralType
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare.CompactCannonMountBlockEntityAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.createbigcannons.MountedAutocannonContraptionMixinDuck
import net.minecraft.server.level.ServerLevel
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity

open class CompactCannonMountMethods : GenericPeripheral {
    override fun id(): String {
        return "${VSAdditionMod.MOD_ID}:cbc_cannon_mount"
    }

    override fun getType(): PeripheralType {
        return PeripheralType.ofAdditional("cbc_cannon_mount")
    }

    @LuaFunction(mainThread = true)
    fun assemble(tileEntity: CompactCannonMountBlockEntity): Any {
        if (!tileEntity.isRunning) {
            (tileEntity as CompactCannonMountBlockEntityAccessor?)?.Assemble()
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
    fun disassemble(tileEntity: CompactCannonMountBlockEntity): Any {
        if (tileEntity.isRunning) {
            tileEntity.disassemble()
            tileEntity.sendData()
            return true
        }
        return false
    }

    @LuaFunction(mainThread = true)
    fun fire(tileEntity: CompactCannonMountBlockEntity) {
        if (tileEntity.contraption?.level() is ServerLevel) {
            (tileEntity.contraption?.contraption as? MountedAutocannonContraptionMixinDuck)?.setIsCalledByComputer()
            (tileEntity.contraption?.contraption as AbstractMountedCannonContraption).fireShot(tileEntity.contraption?.level() as ServerLevel, tileEntity.contraption)
        }
    }

    @LuaFunction(mainThread = true)
    fun isRunning(tileEntity: CompactCannonMountBlockEntity): Boolean {
        return tileEntity.isRunning
    }

    @LuaFunction
    fun getPitch(tileEntity: CompactCannonMountBlockEntity): Double {
        return (tileEntity as CompactCannonMountBlockEntityAccessor).cannonPitch.toDouble()
    }

    @LuaFunction
    fun getYaw(tileEntity: CompactCannonMountBlockEntity): Double {
        return (tileEntity as CompactCannonMountBlockEntityAccessor).cannonYaw.toDouble()
    }

    @LuaFunction
    fun getX(tileEntity: CompactCannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.x
    }

    @LuaFunction
    fun getY(tileEntity: CompactCannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.y
    }

    @LuaFunction
    fun getZ(tileEntity: CompactCannonMountBlockEntity): Int {
        return tileEntity.controllerBlockPos.z
    }

    @LuaFunction
    fun getMaxDepress(tileEntity: CompactCannonMountBlockEntity): Double? {
        return tileEntity.contraption?.maximumDepression()?.toDouble()
    }

    @LuaFunction
    fun getMaxElevate(tileEntity: CompactCannonMountBlockEntity): Double? {
        return tileEntity.contraption?.maximumElevation()?.toDouble()
    }
}