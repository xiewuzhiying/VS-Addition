package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WirelessModemPeripheral.class)
public abstract class MixinWirelessModemPeripheral extends ModemPeripheral{
    protected MixinWirelessModemPeripheral(ModemState state) {
        super(state);
    }
    @Redirect(
            method = "getRange",
            at = @At(
                    value = "INVOKE",
                    target = "Ldan200/computercraft/shared/peripheral/modem/wireless/WirelessModemPeripheral;getPosition()Lnet/minecraft/world/phys/Vec3;"
            )
    )
    public Vec3 vs_addition$getPosition(WirelessModemPeripheral instance){
        final Ship ship = VSGameUtilsKt.getShipManagingPos(instance.getLevel(), instance.getPosition());
        if (ship != null) {
            return  VSGameUtilsKt.toWorldCoordinates(ship, instance.getPosition());
        }
        return instance.getPosition();
    }
}
