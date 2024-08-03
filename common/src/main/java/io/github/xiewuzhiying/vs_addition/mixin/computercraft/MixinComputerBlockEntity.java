package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.VSAdditionCC;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(ComputerBlockEntity.class)
public abstract class MixinComputerBlockEntity {
    @WrapMethod(
            method = "createComputer",
            remap = false
    )
    private ServerComputer vs_addition$addAPI(int id, Operation<ServerComputer> original) {
        ServerComputer computer = original.call(id);
        ServerLevel level = computer.getLevel();
        VSAdditionCC.applyCCAPIs(computer, level);

        return computer;
    }
}