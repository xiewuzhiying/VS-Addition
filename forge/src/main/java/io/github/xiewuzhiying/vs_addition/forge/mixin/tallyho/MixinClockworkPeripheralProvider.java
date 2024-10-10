package io.github.xiewuzhiying.vs_addition.forge.mixin.tallyho;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "edn.stratodonut.tallyho.integration.cc.ClockworkForgePeripheralProviders$ClockworkPeripheralProvider")
public abstract class MixinClockworkPeripheralProvider {
    @Inject(
            method = "getPeripheral",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void undo(Level level, BlockPos blockPos, Direction direction, CallbackInfoReturnable<LazyOptional<IPeripheral>> cir) {
        cir.setReturnValue(LazyOptional.empty());
    }
}
