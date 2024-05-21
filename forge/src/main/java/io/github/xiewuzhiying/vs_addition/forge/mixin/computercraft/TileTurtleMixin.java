package io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft;

import com.llamalad7.mixinextras.sugar.Local;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.VSAdditionCC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileTurtle.class)
public abstract class TileTurtleMixin {
    @Inject(
            method = "createComputer",
            at = @At("RETURN"),
            remap = false
    )
    private void vs_addition$addAPI(int id, CallbackInfoReturnable<ServerComputer> cir, @Local ServerComputer computer) {
        Level level = computer.getLevel();
        VSAdditionCC.applyCCAPIs(computer, (ServerLevel) level);
    }
}
