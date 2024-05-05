package io.github.xiewuzhiying.vs_addition.mixin.vs_eureka;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;

@Mixin(ShipHelmBlockEntity.class)
public interface ShipHelmBlockEntityAccessor {
    @Invoker("getControl")
    EurekaShipControl GetControl();
}
