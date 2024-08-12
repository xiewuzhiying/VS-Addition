package io.github.xiewuzhiying.vs_addition.mixin.create.portable_interface;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.xiewuzhiying.vs_addition.mixinducks.create.portable_interface.IPSIBehavior;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Mixin(PortableStorageInterfaceMovement.class)
public abstract class MixinPortableStorageInterfaceMovement implements MovementBehaviour {

    @WrapOperation(
            method = "findInterface",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceMovement;findStationaryInterface(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceBlockEntity;"
            )
    )
    public PortableStorageInterfaceBlockEntity findStationaryInterface(PortableStorageInterfaceMovement instance, Level level, BlockPos blockPos, BlockState state, Direction direction, Operation<PortableStorageInterfaceBlockEntity> original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            Ship selfShip = VSGameUtilsKt.getShipManagingPos(level, blockPos);
            Vector3d selfDirectionVec = VectorConversionsMCKt.toJOML(context.rotation.apply(Vec3.atLowerCornerOf(context.state
                    .getValue(PortableStorageInterfaceBlock.FACING).getNormal())));

            for (int i = 0; i < 2; i++) {

                Vector3d checkPos = VSGameUtilsKt.toWorldCoordinates(level, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(0.5, 0.5, 0.5));
                if (i != 0) {
                    checkPos = checkPos.add(selfDirectionVec.mul(i - 0.125));
                }
                List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, checkPos.x, checkPos.y, checkPos.z, 2);
                ships.add(VSGameUtilsKt.toWorldCoordinates(level, checkPos));

                for (Vector3d eachShipPos : ships) {
                    PortableStorageInterfaceBlockEntity psi = vs_addition$findPSI(level, eachShipPos);
                    if (psi != null) {
                        Vector3d selfVec = selfDirectionVec.normalize().negate();
                        if (selfShip != null)
                            selfVec = selfVec.rotate(selfShip.getTransform().getShipToWorldRotation());
                        Vector3d directionVec = VectorConversionsMCKt.toJOML(Vec3.atLowerCornerOf(psi.getBlockState()
                                .getValue(PortableStorageInterfaceBlock.FACING).getNormal())).normalize();
                        Ship ship = VSGameUtilsKt.getShipManagingPos(level, eachShipPos);
                        if (ship != null)
                            directionVec = directionVec.rotate(ship.getTransform().getShipToWorldRotation());
                        if (Math.toDegrees(Math.acos(new Vector3d(directionVec).dot(new Vector3d(selfVec)))) <= 22.5)
                            return psi;
                    }
                }
            }
            return null;
        } else {
            return original.call(instance, level, blockPos, state, direction);
        }
    }

    @ModifyExpressionValue(
            method = "findInterface",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 getCenterOf(Vec3 original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            Vec3 transfromedPos = VSGameUtilsKt.toWorldCoordinates(context.world, original);
            Ship ship = VSGameUtilsKt.getShipManagingPos(context.world, context.position);
            if (ship != null)
                transfromedPos = VectorConversionsMCKt.toMinecraft(ship.getTransform().getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(transfromedPos)));
            return transfromedPos;
        } else {
            return original;
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;closerThan(Lnet/minecraft/core/Position;D)Z"
            )
    )
    public boolean closerThan(Vec3 vec, Position pos, double distance, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec.x, vec.y, vec.z, pos.x(), pos.y(), pos.z()) < distance * distance;
        } else {
            return original.call(vec, pos, distance);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;distanceTo(Lnet/minecraft/world/phys/Vec3;)D"
            )
    )
    public double distanceTo(Vec3 vec31, Vec3 vec32, Operation<Double> original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec31.x, vec31.y, vec31.z, vec32.x(), vec32.y(), vec32.z());
        } else {
            return original.call(vec31, vec32);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceMovement;getStationaryInterfaceAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceBlockEntity;"
            )
    )
    public PortableStorageInterfaceBlockEntity redirectToFindPSI(PortableStorageInterfaceMovement instance, Level level, BlockPos pos, BlockState state, Direction direction, Operation<PortableStorageInterfaceBlockEntity> original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            return vs_addition$findPSI(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(pos)));
        } else {
            return original.call(instance, level, pos, state, direction);
        }
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    public double modfiyLength(double original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            return 0;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
            method = "visitNewPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    public double modfiyLength2(double original, @Local(ordinal = 0, argsOnly = true) MovementContext context) {
        if(context.blockEntityData.getInt("ScrollValue") == 1) {
            return 0;
        } else {
            return original;
        }
    }


    @Unique
    public PortableStorageInterfaceBlockEntity vs_addition$findPSI(Level level, Vector3d pos) {
        BlockPos checkThis = new BlockPos(TransformUtilsKt.getToBlockPos(pos));
        if(level.getBlockEntity(checkThis) instanceof PortableStorageInterfaceBlockEntity psi) {
            if(psi.isPowered() || ((IPSIBehavior)psi).vs_addition$getWorkingMode().get() == IPSIBehavior.WorkigMode.ORIGINAL)
                return null;
            return psi;
        }
        return null;
    }
}
