package io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InventoryMethods.class)
public interface InventoryMethodsAccessor {
    @Invoker(value = "moveItem", remap = false)
    static int moveItem(IItemHandler from, int fromSlot, IItemHandler to, int toSlot, int limit) {
        throw new AssertionError();
    }
    @Invoker(value = "extractHandler", remap = false)
    static IItemHandler extractHandler(IPeripheral peripheral) {
        throw new AssertionError();
    }
}
