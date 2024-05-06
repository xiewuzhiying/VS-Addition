package io.github.xiewuzhiying.vs_addition.mixin.computercraft;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WirelessModemPeripheral.class)
public abstract class MixinWirelessModemPeripheral extends ModemPeripheral{
    protected MixinWirelessModemPeripheral(ModemState state) {
        super(state);
    }
    @Inject(
            method = "getRange",
            at = @At(
                    value = "INVOKE",
                    target = "Ldan200/computercraft/shared/peripheral/modem/wireless/WirelessModemPeripheral;getPosition()Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.AFTER
            )
    )
    public void whenPositionOnShip(CallbackInfoReturnable<Double> cir, @Local Level world, @Local LocalRef<Vec3> position){
        final Ship ship = VSGameUtilsKt.getShipManagingPos(world, position.get());
        if (ship != null) {
            position.set(VSGameUtilsKt.toWorldCoordinates(ship, position.get()));
        }
    }
}
