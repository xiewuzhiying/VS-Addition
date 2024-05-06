package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.xiewuzhiying.vs_addition.mixin.createbigcannons.CannonMountBlockEntityAccessor;
import io.github.xiewuzhiying.vs_addition.mixin.vs_eureka.ShipHelmBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;

public class ShipHelmPeripheral implements IPeripheral {

    private final String type ;
    private final ShipHelmBlockEntity tileEntity;

    private final EurekaShipControl shipControl;

    public ShipHelmPeripheral(String type, ShipHelmBlockEntity tileEntity, Level level, BlockPos blockPos) {
        this.type = type;
        this.tileEntity = tileEntity;
        this.shipControl = ((ShipHelmBlockEntityAccessor)(Object) this.tileEntity).GetControl();
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral == this;
    }

    @Override
    public Object getTarget() {
        return this.tileEntity;
    }

    @LuaFunction(mainThread = true)
    public final Object disassemble() {
        if(this.tileEntity.getAssembled()){
            this.tileEntity.disassemble();
            return true;
        }
        return false;
    }
}
