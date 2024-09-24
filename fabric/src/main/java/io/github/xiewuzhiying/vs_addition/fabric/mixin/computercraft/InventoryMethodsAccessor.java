package io.github.xiewuzhiying.vs_addition.fabric.mixin.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InventoryMethods.class)
public interface InventoryMethodsAccessor {
    @Invoker(value = "moveItem", remap = false)
    static int moveItem(SlottedStorage<ItemVariant> from, int fromSlot, SlottedStorage<ItemVariant> to, int toSlot, final int limit) {
        throw new AssertionError();
    }
    @Invoker(value = "toStack", remap = false)
    static ItemStack toStack(SingleSlotStorage<ItemVariant> variant) {
        throw new AssertionError();
    }
    @Invoker(value = "extractHandler", remap = false)
    static SlottedStorage<ItemVariant> extractHandler(IPeripheral peripheral) {
        throw new AssertionError();
    }
}
