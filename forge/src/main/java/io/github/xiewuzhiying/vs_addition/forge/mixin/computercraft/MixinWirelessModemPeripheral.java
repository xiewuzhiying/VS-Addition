package io.github.xiewuzhiying.vs_addition.forge.mixin.computercraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WirelessModemPeripheral.class)
public abstract class MixinWirelessModemPeripheral extends ModemPeripheral{
    protected MixinWirelessModemPeripheral(ModemState state) {
        super(state);
    }
    @WrapOperation(
            method = "getRange",
            at = @At(
                    value = "INVOKE",
                    target = "Ldan200/computercraft/shared/peripheral/modem/wireless/WirelessModemPeripheral;getPosition()Lnet/minecraft/world/phys/Vec3;"
            ),
            remap = false
    )
    public Vec3 vs_addition$getPosition(WirelessModemPeripheral instance, Operation<Vec3> original){
        return VSGameUtilsKt.toWorldCoordinates(instance.getLevel(), original.call(instance));
    }
}
