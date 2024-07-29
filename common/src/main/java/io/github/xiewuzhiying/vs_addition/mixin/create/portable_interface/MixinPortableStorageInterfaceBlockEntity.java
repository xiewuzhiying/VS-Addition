package io.github.xiewuzhiying.vs_addition.mixin.create.portable_interface;

import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.psi.IPSIBehavior;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Pseudo
@Mixin(PortableStorageInterfaceBlockEntity.class)
public abstract class MixinPortableStorageInterfaceBlockEntity extends SmartBlockEntity implements IPSIBehavior {
    @Shadow(remap = false) protected float distance;
    @Shadow(remap = false) public abstract void startConnecting();
    @Shadow(remap = false) abstract boolean isConnected();
    @Shadow(remap = false) protected abstract Integer getTransferTimeout();

    @Shadow(remap = false) public int keepAlive;
    @Shadow(remap = false) protected int transferTimer;
    @Shadow(remap = false) protected boolean powered;
    @Shadow(remap = false) protected LerpedFloat connectionAnimation;

    @Shadow(remap = false) @Final public static int ANIMATION;
    @Unique
    private boolean isPassive;

    @Unique
    private PortableStorageInterfaceBlockEntity connectedPI;

    @Unique
    protected ScrollOptionBehaviour<IPSIBehavior.WorkigMode> workingMode;


    public MixinPortableStorageInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.connectedPI = null;
        this.isPassive = false;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    public void tick(CallbackInfo ci) {
        if(((IPSIBehavior)this).vs_addition$getWorkingMode().get() == IPSIBehavior.WorkigMode.WITH_SHIP) {
            if(this.isPassive) {
                super.tick();
                boolean wasConnected = isConnected();
                int timeUnit = getTransferTimeout();
                int animation = ANIMATION;

                if (keepAlive > 0) {
                    keepAlive--;
                    if (keepAlive == 0 && !level.isClientSide) {
                        vs_addition$stopTransferring();
                        transferTimer = ANIMATION - 1;
                        sendData();
                        ci.cancel();
                        return;
                    }
                }

                transferTimer = Math.min(transferTimer, ANIMATION * 2 + timeUnit);

                boolean timerCanDecrement = transferTimer > ANIMATION || transferTimer > 0 && keepAlive == 0
                        && (isVirtual() || !level.isClientSide || transferTimer != ANIMATION);

                if (timerCanDecrement && (!isVirtual() || transferTimer != ANIMATION)) {
                    transferTimer--;
                    if (transferTimer == ANIMATION - 1)
                        sendData();
                    if (transferTimer <= 0 || powered)
                        vs_addition$stopTransferring();
                }

                boolean isConnected = isConnected();
                if (wasConnected != isConnected && !level.isClientSide)
                    setChanged();

                float progress = 0;
                if (isConnected)
                    progress = 1;
                else if (transferTimer >= timeUnit + animation)
                    progress = Mth.lerp((transferTimer - timeUnit - animation) / (float) animation, 1, 0);
                else if (transferTimer < animation)
                    progress = Mth.lerp(transferTimer / (float) animation, 0, 1);
                connectionAnimation.setValue(progress);
                ci.cancel();
            } else {
                super.tick();
                PortableStorageInterfaceBlockEntity stationaryInterface =
                        getStationaryInterfaceAt((PortableStorageInterfaceBlockEntity)(Object)this, level, getBlockPos());
                if (stationaryInterface == null) {
                    this.connectedPI = null;
                    ci.cancel();
                    return;
                }

                if (((IPSIBehavior)stationaryInterface).vs_addition$getConnectedPI() == null)
                    ((IPSIBehavior)stationaryInterface).vs_addition$startTransferringTo((PortableStorageInterfaceBlockEntity)(Object)this, ((MixinPortableStorageInterfaceBlockEntity)(Object)stationaryInterface).distance);

                stationaryInterface.keepAlive = 2;
                ci.cancel();
            }
        }
    }




    @Override
    public void vs_addition$startTransferringTo(PortableStorageInterfaceBlockEntity pi, float distance) {
        if (this.connectedPI == pi)
            return;
        this.distance = Math.min(2, distance);
        this.connectedPI = pi;
        this.isPassive = true;
        startConnecting();
        notifyUpdate();
    }

    @Override
    public void vs_addition$stopTransferring() {
        this.connectedPI = null;
        this.isPassive = false;
        level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
    }

    @Override
    public boolean vs_addition$canTransfer() {
        if (this.connectedPI != null)
            vs_addition$stopTransferring();
        return this.connectedPI != null && isConnected();
    }

    @Override
    public PortableStorageInterfaceBlockEntity vs_addition$getConnectedPI() {
        return this.connectedPI;
    }

    @Inject(
            method = "addBehaviours",
            at = @At("RETURN"),
            remap = false
    )
    public void behaviour(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        this.workingMode = new ScrollOptionBehaviour<>(IPSIBehavior.WorkigMode.class, Lang.translateDirect("vs_addition.working_mode"), (PortableStorageInterfaceBlockEntity)(Object) this, vs_addition$getMovementModeSlot());
        behaviours.add(this.workingMode);
    }

    @Unique
    private ValueBoxTransform vs_addition$getMovementModeSlot() {
        return new DirectionalExtenderScrollOptionSlot((state, d) -> {
            Direction.Axis axis = d.getAxis();
            Direction.Axis bearingAxis = state.getValue(PortableStorageInterfaceBlock.FACING)
                    .getAxis();
            return bearingAxis != axis;
        });
    }

    @Override
    public ScrollOptionBehaviour<IPSIBehavior.WorkigMode> vs_addition$getWorkingMode() {
        return workingMode;
    }

    public PortableStorageInterfaceBlockEntity getStationaryInterfaceAt(PortableStorageInterfaceBlockEntity self, Level level, BlockPos blockPos) {
        Ship selfShip = VSGameUtilsKt.getShipManagingPos(level, blockPos);
        Vector3d selfDirectionVec =VectorConversionsMCKt.toJOML(Vec3.atLowerCornerOf(self.getBlockState()
                .getValue(PortableStorageInterfaceBlock.FACING).getNormal())).normalize();

        for (int i = 0; i < 2; i++) {

            Vector3d checkPos = VSGameUtilsKt.toWorldCoordinates(level, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(0.5, 0.5, 0.5));
            if (i!=0){
                checkPos = checkPos.add(selfDirectionVec.mul(i-0.125));
            }
            List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, checkPos.x, checkPos.y, checkPos.z, 2);
            ships.add(VSGameUtilsKt.toWorldCoordinates(level, checkPos));

            for(Vector3d eachShipPos : ships) {
                PortableStorageInterfaceBlockEntity psi = findPSI(level, eachShipPos);
                if (psi != null) {
                    Vector3d selfVec = selfDirectionVec.normalize().negate();
                    if (selfShip!=null)
                        selfVec = selfVec.rotate(selfShip.getTransform().getShipToWorldRotation());
                    Vector3d directionVec = VectorConversionsMCKt.toJOML(Vec3.atLowerCornerOf(psi.getBlockState()
                            .getValue(PortableStorageInterfaceBlock.FACING).getNormal())).normalize();
                    Ship ship = VSGameUtilsKt.getShipManagingPos(level, eachShipPos);
                    if (ship!=null)
                        directionVec = directionVec.rotate(ship.getTransform().getShipToWorldRotation());
                    if (Math.toDegrees(Math.acos(new Vector3d(directionVec).dot(new Vector3d(selfVec))))  <= 10)
                        return psi;
                }
            }

//            level.addParticle(ParticleTypes.END_ROD, checkPos.x, checkPos.y, checkPos.z,
//                    0, 0, 0);
        }
        return null;
    }

    @Unique
    public PortableStorageInterfaceBlockEntity findPSI(Level level, Vector3d pos) {
        BlockPos checkThis = new BlockPos(TransformUtils.floorToBlockPos(pos));
        if(level.getBlockEntity(checkThis) instanceof PortableStorageInterfaceBlockEntity psi) {
            if(psi.isPowered() || ((IPSIBehavior)psi).vs_addition$getWorkingMode().get() == WorkigMode.ORIGINAL)
                return null;
            return psi;
        }
        return null;
    }
}
