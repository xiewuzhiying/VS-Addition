package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.chassis.StickerBlockEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.util.transformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint;
import org.valkyrienskies.core.apigame.constraints.VSFixedOrientationConstraint;
import org.valkyrienskies.core.apigame.world.ServerShipWorldCore;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

@Mixin(StickerBlockEntity.class)
public abstract class MixinStickerBlockEntity extends SmartBlockEntity {

    @Shadow public abstract boolean isBlockStateExtended();

    @Unique
    private final Map<BlockPos, List<Integer>> map = new HashMap<>();

    @Unique
    private final double compliance = VSAdditionConfig.SERVER.getStickerCompliance();
    @Unique
    private final double maxForce = VSAdditionConfig.SERVER.getStickerMaxForce();

    @Unique
    private boolean needUpdate = false;

    @Unique
    private boolean wasBlockStateExtended = false;

    public MixinStickerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    public void stickerConstraints(CallbackInfo ci) {

        if(!level.isClientSide && isBlockStateExtended()) {
            ServerShipWorldCore serverShipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
            List<BlockPos> removal = new ArrayList<>();
            for (Map.Entry<BlockPos, List<Integer>> entry : map.entrySet()) {
                BlockPos blockPos = entry.getKey();
                if(isAirOrFluid(level.getBlockState(blockPos))) {
                    List<Integer> constraints = entry.getValue();
                    for (Integer constraint : constraints) {
                        serverShipWorldCore.removeConstraint(constraint);
                    }
                    removal.add(blockPos);
                }
            }
            removal.forEach(map.keySet()::remove);
        }

        if(isBlockStateExtended() != this.wasBlockStateExtended) {
            this.needUpdate = true;
            this.wasBlockStateExtended = isBlockStateExtended();
        }

        if (this.needUpdate && this.wasBlockStateExtended) {

            Vector3d selfPosOnShip = new Vector3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()).add(0.5, 0.5, 0.5);
            Quaterniondc rotationQuaternion = transformUtils.directionToQuaterniond(getBlockState().getValue(FACING));
            Vector3d attachmentOffset1 = rotationQuaternion.transform(new Vector3d(0.0, 0.5625, 0.0));
            Vector3d attachmentLocalPos1 = selfPosOnShip.add(attachmentOffset1);

            Vector3d find = VSGameUtilsKt.toWorldCoordinates(level, attachmentLocalPos1);
            List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, find.x, find.y, find.z, 0.25);
            ships.add(find);

            if(!level.isClientSide) {
                ServerShipWorldCore serverShipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
                ServerShip selfShip = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, getBlockPos());
                for (Vector3d posOnShip : ships) {
                    BlockPos blockPos = transformUtils.floorToBlockPos(posOnShip);
                    BlockState state = level.getBlockState(blockPos);
                    if(isAirOrFluid(state))
                        continue;
                    ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, posOnShip);
                    if(ship == selfShip)
                        continue;
                    long objectId1;
                    long objectId2;
                    Vector3d localPos1;
                    Vector3d localPos2;
                    Quaterniond localRot1;
                    Quaterniond localRot2;
                    if(selfShip==null) {
                        objectId1 = ship.getId();
                        objectId2 = serverShipWorldCore.getDimensionToGroundBodyIdImmutable().get(VSGameUtilsKt.getDimensionId(level));
                        localPos1 = (Vector3d) ship.getTransform().getPositionInShip();
                        localPos2 = (Vector3d) ship.getTransform().getPositionInWorld();
                        localRot1 = (Quaterniond) ship.getTransform().getShipToWorldRotation();
                        localRot2 = new Quaterniond();
                    } else if (ship==null) {
                        objectId1 = serverShipWorldCore.getDimensionToGroundBodyIdImmutable().get(VSGameUtilsKt.getDimensionId(level));
                        objectId2 = selfShip.getId();
                        localPos1 = (Vector3d) selfShip.getTransform().getPositionInWorld();
                        localPos2 = (Vector3d) selfShip.getTransform().getPositionInShip();
                        localRot1 = new Quaterniond();
                        localRot2 = (Quaterniond) selfShip.getTransform().getShipToWorldRotation();
                    } else {
                        objectId1 = ship.getId();
                        objectId2 = selfShip.getId();
                        localPos1 = (Vector3d) ship.getTransform().getPositionInShip();
                        localPos2 = selfShip.getTransform().getWorldToShip().transformPosition((Vector3d) ship.getTransform().getPositionInWorld());
                        localRot1 = (Quaterniond) ship.getTransform().getShipToWorldRotation();
                        localRot2 = (Quaterniond) selfShip.getTransform().getShipToWorldRotation();
                    }
                    VSAttachmentConstraint aconstraint = new VSAttachmentConstraint(objectId1, objectId2, this.compliance, localPos1, localPos2, this.maxForce, 0.0);
                    VSFixedOrientationConstraint rconstraint = new VSFixedOrientationConstraint(objectId1, objectId2, this.compliance, localRot1.invert(new Quaterniond()), localRot2.invert(new Quaterniond()), this.maxForce);
                    List<Integer> constraints = new ArrayList<>();
                    Integer aconstraintId = serverShipWorldCore.createNewConstraint(aconstraint);
                    Integer rconstraintId = serverShipWorldCore.createNewConstraint(rconstraint);
                    constraints.add(aconstraintId);
                    constraints.add(rconstraintId);
                    this.map.put(blockPos, constraints);
                }
            }
            if(level.isClientSide && ships.size() > 0) {
                SuperGlueItem.spawnParticles(level, worldPosition, getBlockState().getValue(StickerBlock.FACING), true);
                playSound(true);
            }
            this.needUpdate = false;
        }
        if (this.needUpdate) {
            clearConstraint();
            if(level.isClientSide) {
                playSound(false);
            }
            this.needUpdate = false;
        }
    }

    @Override
    public void remove() {
        clearConstraint();
        super.remove();
    }

    @Unique
    public void clearConstraint() {
        if(!level.isClientSide) {
            ServerShipWorldCore serverShipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
            for (Map.Entry<BlockPos, List<Integer>> entry : map.entrySet()) {
                List<Integer> constraints = entry.getValue();
                for (Integer constraint : constraints) {
                    serverShipWorldCore.removeConstraint(constraint);
                }
            }
            map.clear();
        }
    }

    @Unique
    public boolean isAirOrFluid(BlockState state) {
        return state.isAir() || state.getFluidState() != Fluids.EMPTY.defaultFluidState();
    }

    @Shadow
    public abstract void playSound(boolean attach);
}

