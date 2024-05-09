package io.github.xiewuzhiying.vs_addition.compats.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.eureka.EurekaConfig;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.eureka.util.ShipAssembler;
import org.valkyrienskies.mod.api.SeatedControllingPlayer;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ShipHelmPeripheral implements IPeripheral {

    public final String type ;
    public final ShipHelmBlockEntity tileEntity;

    public final Level level;

    public final BlockPos pos;

    public ShipHelmPeripheral(String type, ShipHelmBlockEntity tileEntity, Level level, BlockPos blockPos) {
        this.type = type;
        this.tileEntity = tileEntity;
        this.level = level;
        this.pos = blockPos;
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
    public final void disassemble() throws LuaException {
        if(this.tileEntity.getAssembled()){
            this.tileEntity.disassemble();
        } else throw new LuaException("Not assembled yet");
    }

    @LuaFunction(mainThread = true)
    public final void assemble() throws LuaException {
        if(!this.tileEntity.getAssembled()){
            ServerShip builtShip = ShipAssembler.INSTANCE.collectBlocks(
                    (ServerLevel) this.level,
                    this.pos,
                    blockState -> !blockState.isAir() && !EurekaConfig.SERVER.getBlockBlacklist().contains(Registry.BLOCK.getKey(blockState.getBlock()).toString())
            );
            if(builtShip == null)
                throw new LuaException("Ship is too big! Max size is" + EurekaConfig.SERVER.getMaxShipBlocks() + "blocks (changable in the config)");
        } else throw new LuaException("Already assembled");
    }

    @LuaFunction
    public final void align() throws LuaException {
        if(this.tileEntity.getAssembled()) {
            this.tileEntity.align();
        } else throw new LuaException("Not assembled yet");
    }

    @LuaFunction
    public final void move(IArguments args) throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null)
            fakePlayer = new SeatedControllingPlayer(this.level.getBlockState(this.pos).getValue(HORIZONTAL_FACING).getOpposite());
        ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
        fakePlayer.setLeftImpulse(Math.min(Math.max((float) args.getDouble(0), -1), 1));
        fakePlayer.setUpImpulse(Math.min(Math.max((float) args.getDouble(1), -1), 1));
        fakePlayer.setForwardImpulse(Math.min(Math.max((float) args.getDouble(2), -1), 1));
    }

    @LuaFunction
    public final int getBalloonAmount() throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        return control.getBalloons();
    }

    @LuaFunction
    public final int getAnchorAmount() throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        return control.getAnchors();
    }

    @LuaFunction
    public final int getActiveAnchorAmount() throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        return control.getAnchorsActive();
    }

    @LuaFunction
    public final boolean areAnchorsActive() throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        return control.getAnchorsActive() > 0;
    }

    @LuaFunction
    public final int getShipHelmAmount() throws LuaException {
        if (this.level.isClientSide()) {
            throw new LuaException("client");
        }
        ServerShip ship = (ServerShip) VSGameUtilsKt.getShipManagingPos(this.level, this.pos);
        if (ship == null) {
            throw new LuaException("No ship");
        }
        EurekaShipControl control = (EurekaShipControl) ship.getAttachment(EurekaShipControl.class);
        if (control == null) {
            throw new LuaException("Not Eureka ship");
        }
        return control.getHelms();
    }

}
