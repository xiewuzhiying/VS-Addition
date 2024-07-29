package io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft;

import dan200.computercraft.shared.computer.blocks.TileComputer;
import dan200.computercraft.shared.computer.core.ServerComputer;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.VSAdditionCC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(TileComputer.class)
public abstract class TileComputerMixin {
    @Inject(
            method = "createComputer",
            at = @At("RETURN"),
            remap = false
    )
    private void vs_addition$addAPI(int id, CallbackInfoReturnable<ServerComputer> cir) {
        ServerComputer computer = cir.getReturnValue();
        ServerLevel level = computer.getLevel();
        VSAdditionCC.applyCCAPIs(computer, level);
    }
}