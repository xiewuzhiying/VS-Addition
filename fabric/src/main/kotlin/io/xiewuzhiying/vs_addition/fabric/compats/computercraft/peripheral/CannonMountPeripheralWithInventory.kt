package io.xiewuzhiying.vs_addition.fabric.compats.computercraft.peripheral

import dan200.computercraft.api.detail.VanillaDetailRegistries
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.core.util.ArgumentHelpers
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods.StorageWrapper
import io.github.xiewuzhiying.vs_addition.compats.computercraft.peripheral.CannonMountPeripheral
import io.github.xiewuzhiying.vs_addition.fabric.mixin.computercraft.InventoryMethodsAccessor
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity
import java.util.*

class CannonMountPeripheralWithInventory(peripheralType: String, tileEntity: CannonMountBlockEntity, val inventory: StorageWrapper) : CannonMountPeripheral(peripheralType,
    tileEntity
){
    @LuaFunction(mainThread = true)
    fun size(): Int {
        return inventory.storage.slots.size
    }

    @LuaFunction(mainThread = true)
    fun list(): Map<Int, Map<String, *>> {
        val result: MutableMap<Int, Map<String, *>> = HashMap()
        val slots = inventory.storage.slots
        val size = slots.size
        for (i in 0 until size) {
            val stack = InventoryMethodsAccessor.toStack(slots[i])
            if (!stack.isEmpty) result[i + 1] = VanillaDetailRegistries.ITEM_STACK.getBasicDetails(stack)
        }

        return result
    }

    @LuaFunction(mainThread = true)
    fun getItemDetail(slot: Int): Map<String, *>? {
        ArgumentHelpers.assertBetween(slot, 1, inventory.storage.slotCount, "Slot out of range (%s)")

        val stack = InventoryMethodsAccessor.toStack(inventory.storage.getSlot(slot - 1))
        return if (stack.isEmpty) null else VanillaDetailRegistries.ITEM_STACK.getDetails(stack)
    }

    @LuaFunction(mainThread = true)
    fun getItemLimit(slot: Int): Long {
        ArgumentHelpers.assertBetween(slot, 1, inventory.storage.slotCount, "Slot out of range (%s)")
        return inventory.storage.getSlot(slot - 1).capacity
    }

    @LuaFunction(mainThread = true)
    fun pushItems(
        computer: IComputerAccess,
        toName: String, fromSlot: Int, limit: Optional<Int>, toSlot: Optional<Int>
    ): Int {
        // Find location to transfer to
        val location = computer.getAvailablePeripheral(toName)
            ?: throw LuaException("Target '$toName' does not exist")

        val to = InventoryMethodsAccessor.extractHandler(location)
            ?: throw LuaException("Target '$toName' is not an inventory")

        val fromStorage = inventory.storage

        // Validate slots
        val actualLimit = limit.orElse(Int.MAX_VALUE)
        ArgumentHelpers.assertBetween(fromSlot, 1, fromStorage.slotCount, "From slot out of range (%s)")
        if (toSlot.isPresent) ArgumentHelpers.assertBetween(toSlot.get(), 1, to.slots.size, "To slot out of range (%s)")

        if (actualLimit <= 0) return 0
        return InventoryMethodsAccessor.moveItem(fromStorage, fromSlot - 1, to, toSlot.orElse(0) - 1, actualLimit)
    }

    @LuaFunction(mainThread = true)
    fun pullItems(
        computer: IComputerAccess,
        fromName: String, fromSlot: Int, limit: Optional<Int>, toSlot: Optional<Int>
    ): Int {
        // Find location to transfer to
        val location = computer.getAvailablePeripheral(fromName)
            ?: throw LuaException("Source '$fromName' does not exist")

        val toStorage = inventory.storage

        val from = InventoryMethodsAccessor.extractHandler(location)
            ?: throw LuaException("Source '$fromName' is not an inventory")

        // Validate slots
        val actualLimit = limit.orElse(Int.MAX_VALUE)
        ArgumentHelpers.assertBetween(fromSlot, 1, from.slots.size, "From slot out of range (%s)")
        if (toSlot.isPresent) ArgumentHelpers.assertBetween(
            toSlot.get(),
            1,
            toStorage.slotCount,
            "To slot out of range (%s)"
        )

        if (actualLimit <= 0) return 0
        return InventoryMethodsAccessor.moveItem(from, fromSlot - 1, toStorage, toSlot.orElse(0) - 1, actualLimit)
    }
}