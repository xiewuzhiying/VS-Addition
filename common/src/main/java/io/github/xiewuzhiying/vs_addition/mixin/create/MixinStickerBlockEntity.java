package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.chassis.StickerBlockEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;
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

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

@Mixin(StickerBlockEntity.class)
public abstract class MixinStickerBlockEntity extends SmartBlockEntity {

    public MixinStickerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow public abstract boolean isBlockStateExtended();

    @Unique
    private final List<Integer> constraints = new ArrayList<>();

    @Unique
    private static final double vs_addition$compliance = VSAdditionConfig.SERVER.getStickerCompliance();
    @Unique
    private static final double vs_addition$maxForce = VSAdditionConfig.SERVER.getStickerMaxForce();

    @Unique
    private boolean vs_addition$needUpdate = false;

    @Unique
    private boolean wasBlockStateExtended = false;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    public void stickerConstraints(CallbackInfo ci) {
        if(isBlockStateExtended() != wasBlockStateExtended) {
            vs_addition$needUpdate = true;
            wasBlockStateExtended = isBlockStateExtended();
        }
        if (vs_addition$needUpdate && wasBlockStateExtended) {
            Vector3d selfPosOnShip = new Vector3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()).add(0.5, 0.5, 0.5);
            Quaterniondc rotationQuaternion = getRotationQuaternion(getBlockState().getValue(FACING));
            Vector3d attachmentOffset1 = rotationQuaternion.transform(new Vector3d(0.0, 0.5625, 0.0));
            Vector3d attachmentLocalPos1 = selfPosOnShip.add(attachmentOffset1);
            Vector3d find = VSGameUtilsKt.toWorldCoordinates(level, attachmentLocalPos1);
            List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, find.x, find.y, find.z, 0.25);
            ships.add(find);
            if(!level.isClientSide) {
                ServerShipWorldCore serverShipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
                ServerShip selfShip = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, getBlockPos());
                for (Vector3d posOnShip : ships) {
                    Vector3d blockPos = posOnShip.floor();
                    if(level.getBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z)) == Blocks.AIR.defaultBlockState()) {
                        continue;
                    }
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
                    VSAttachmentConstraint aconstraint = new VSAttachmentConstraint(objectId1, objectId2, vs_addition$compliance, localPos1, localPos2, vs_addition$maxForce, 0.0);
                    VSFixedOrientationConstraint rconstraint = new VSFixedOrientationConstraint(objectId1, objectId2, vs_addition$compliance, localRot1.invert(new Quaterniond()), localRot2.invert(new Quaterniond()), vs_addition$maxForce);
                    Integer aconstraintId = serverShipWorldCore.createNewConstraint(aconstraint);
                    Integer rconstraintId = serverShipWorldCore.createNewConstraint(rconstraint);
                    this.constraints.add(aconstraintId);
                    this.constraints.add(rconstraintId);
                }
            }
            if(level.isClientSide && ships.size() > 0) {
                SuperGlueItem.spawnParticles(level, worldPosition, getBlockState().getValue(StickerBlock.FACING), true);
                playSound(true);
            }
            vs_addition$needUpdate = false;
        }
        if (vs_addition$needUpdate) {
            if(!level.isClientSide) {
                ServerShipWorldCore serverShipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
                for (Integer constraint : this.constraints) {
                    serverShipWorldCore.removeConstraint(constraint);
                }
                this.constraints.removeAll(this.constraints);
            }
            if(level.isClientSide) {
                playSound(false);
            }
            vs_addition$needUpdate = false;
        }
    }

    @Unique
    private Quaterniondc getRotationQuaternion(Direction facing) {
        switch (facing) {
            case DOWN -> {
                return new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(1.0, 0.0, 0.0)));
            }
            case NORTH -> {
                return new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
            }
            case EAST -> {
                return new Quaterniond(new AxisAngle4d(0.5 * Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
            }
            case SOUTH -> {
                return new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0))).normalize();
            }
            case WEST -> {
                return new Quaterniond(new AxisAngle4d(1.5 * Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
            }
            case UP -> {
                // Do nothing
                return new Quaterniond();
            }
        }
        return null;
    }

    @Shadow
    public abstract void playSound(boolean attach);
}

