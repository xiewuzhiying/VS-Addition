package io.github.xiewuzhiying.vs_addition.fabric.mixin.computercraft;

import com.llamalad7.mixinextras.sugar.Local;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import io.github.xiewuzhiying.vs_addition.compats.computercraft.VSAdditionCC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleBlockEntity.class)
public class TileTurtleMixin {
    @Inject(
            method = "createComputer",
            at = @At(
                    value = "INVOKE",
                    target = "Ldan200/computercraft/shared/computer/core/ServerComputer;addAPI(Ldan200/computercraft/api/lua/ILuaAPI;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void vs_addition$addAPI(int instanceID, int id, CallbackInfoReturnable<ServerComputer> cir, @Local ServerComputer computer) {
        Level level = computer.getLevel();
        VSAdditionCC.applyCCAPIs(computer, (ServerLevel) level);
    }
}
