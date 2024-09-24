package io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.peripherals

import dan200.computercraft.api.detail.VanillaDetailRegistries
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.core.util.ArgumentHelpers
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CheatCannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft.InventoryMethodsAccessor
import net.minecraftforge.items.IItemHandler
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import java.util.*

class CheatCannonMountPeripheralWithInventory(peripheralType: String, tileEntity: CannonMountBlockEntity, val inventory: IItemHandler) : CheatCannonMountPeripheral(peripheralType,
    tileEntity
){
    @LuaFunction(mainThread = true)
    fun size(): Int {
        return inventory.slots
    }

    @LuaFunction(mainThread = true)
    fun list(): Map<Int, Map<String, *>> {
        val result: MutableMap<Int, Map<String, *>> = HashMap()
        val size = inventory.slots
        for (i in 0 until size) {
            val stack = inventory.getStackInSlot(i)
            if (!stack.isEmpty) result[i + 1] = VanillaDetailRegistries.ITEM_STACK.getBasicDetails(stack)
        }

        return result
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun getItemDetail(slot: Int): Map<String, *>? {
        ArgumentHelpers.assertBetween(slot, 1, inventory.slots, "Slot out of range (%s)")

        val stack = inventory.getStackInSlot(slot - 1)
        return if (stack.isEmpty) null else VanillaDetailRegistries.ITEM_STACK.getDetails(stack)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun getItemLimit(slot: Int): Long {
        ArgumentHelpers.assertBetween(slot, 1, inventory.slots, "Slot out of range (%s)")
        return inventory.getSlotLimit(slot - 1).toLong()
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun pushItems(
        computer: IComputerAccess,
        toName: String, fromSlot: Int, limit: Optional<Int>, toSlot: Optional<Int>
    ): Int {
        // Find location to transfer to
        val location = computer.getAvailablePeripheral(toName)
            ?: throw LuaException("Target '$toName' does not exist")

        val to = InventoryMethodsAccessor.extractHandler(location)
            ?: throw LuaException("Target '$toName' is not an inventory")


        // Validate slots
        val actualLimit = limit.orElse(Int.MAX_VALUE)
        ArgumentHelpers.assertBetween(fromSlot, 1, inventory.slots, "From slot out of range (%s)")
        if (toSlot.isPresent) ArgumentHelpers.assertBetween(toSlot.get(), 1, to.slots, "To slot out of range (%s)")

        if (actualLimit <= 0) return 0
        return InventoryMethodsAccessor.moveItem(inventory, fromSlot - 1, to, toSlot.orElse(0) - 1, actualLimit)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun pullItems(
        computer: IComputerAccess,
        fromName: String, fromSlot: Int, limit: Optional<Int>, toSlot: Optional<Int>
    ): Int {
        // Find location to transfer to
        val location = computer.getAvailablePeripheral(fromName)
            ?: throw LuaException("Source '$fromName' does not exist")

        val from = InventoryMethodsAccessor.extractHandler(location)
            ?: throw LuaException("Source '$fromName' is not an inventory")


        // Validate slots
        val actualLimit = limit.orElse(Int.MAX_VALUE)
        ArgumentHelpers.assertBetween(fromSlot, 1, from.slots, "From slot out of range (%s)")
        if (toSlot.isPresent) ArgumentHelpers.assertBetween(toSlot.get(), 1, inventory.slots, "To slot out of range (%s)")

        if (actualLimit <= 0) return 0
        return InventoryMethodsAccessor.moveItem(from, fromSlot - 1, inventory, toSlot.orElse(0) - 1, actualLimit)
    }
}