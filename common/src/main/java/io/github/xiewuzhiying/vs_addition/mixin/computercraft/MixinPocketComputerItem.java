package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.VSAdditionCC;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(PocketComputerItem.class)
public abstract class MixinPocketComputerItem {
    @WrapMethod(
            method = "getServerComputer(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/item/ItemStack;)Ldan200/computercraft/shared/pocket/core/PocketServerComputer;"
    )
    private static PocketServerComputer vs_addition$addAPI(MinecraftServer server, ItemStack stack, Operation<PocketServerComputer> original) {
        PocketServerComputer computer = original.call(server, stack);
        ServerLevel level = computer.getLevel();
        VSAdditionCC.applyCCAPIs(computer, level);

        return computer;
    }
}
