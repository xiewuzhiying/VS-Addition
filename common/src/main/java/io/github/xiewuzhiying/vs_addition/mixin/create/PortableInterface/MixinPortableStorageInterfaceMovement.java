package io.github.xiewuzhiying.vs_addition.mixin.create.PortableInterface;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.xiewuzhiying.vs_addition.util.transformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    public PortableStorageInterfaceBlockEntity findStationaryInterface(PortableStorageInterfaceMovement instance, Level level, BlockPos blockPos, BlockState state, Direction direction, Operation<PortableStorageInterfaceBlockEntity> original, @Local(ordinal = 0) MovementContext context) {
        Ship selfShip = VSGameUtilsKt.getShipManagingPos(level, blockPos);
        Vector3d selfDirectionVec = VectorConversionsMCKt.toJOML(context.rotation.apply(Vec3.atLowerCornerOf(context.state
                .getValue(PortableStorageInterfaceBlock.FACING).getNormal())));

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
                    if (directionVec.sub(selfVec).length()  <= 0.25)
                        return psi;
                }
            }

//            level.addParticle(ParticleTypes.END_ROD, checkPos.x, checkPos.y, checkPos.z,
//                    0, 0, 0);
        }
        return null;
    }

    @WrapOperation(
            method = "findInterface",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private static Vec3 getCenterOf(Vec3i pos, Operation<Vec3> original, @Local(ordinal = 0) MovementContext context) {
        Vec3 transfromedPos = VSGameUtilsKt.toWorldCoordinates(context.world, original.call(pos));
        Ship ship = VSGameUtilsKt.getShipManagingPos(context.world, context.position);
        if(ship!=null)
            transfromedPos = VectorConversionsMCKt.toMinecraft(ship.getTransform().getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(transfromedPos)));
        return transfromedPos;
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;closerThan(Lnet/minecraft/core/Position;D)Z"
            )
    )
    public boolean closerThan(Vec3 vec, Position pos, double distance, @Local(ordinal = 0) MovementContext context) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec.x, vec.y, vec.z, pos.x(), pos.y(), pos.z()) < distance * distance;
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;distanceTo(Lnet/minecraft/world/phys/Vec3;)D"
            )
    )
    public double distanceTo(Vec3 vec1, Vec3 vec2, @Local(ordinal = 0) MovementContext context) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec1.x, vec1.y, vec1.z, vec2.x(), vec2.y(), vec2.z());
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceMovement;getStationaryInterfaceAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceBlockEntity;"
            )
    )
    public PortableStorageInterfaceBlockEntity redirectToFindPSI(PortableStorageInterfaceMovement instance, Level level, BlockPos pos, BlockState state, Direction direction) {
        return findPSI(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(pos)));
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    public double modfiyLength(Vec3 instance) {
        return 0;
    }

    @Redirect(
            method = "visitNewPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    public double modfiyLength2(Vec3 instance) {
        return 0;
    }


    @Unique
    public PortableStorageInterfaceBlockEntity findPSI(Level level, Vector3d pos) {
        BlockPos checkThis = new BlockPos(transformUtils.floorToBlockPos(pos));
        if(level.getBlockEntity(checkThis) instanceof PortableStorageInterfaceBlockEntity psi) {
            if(psi.isPowered())
                return null;
//            level.addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z,
//                    0, 0, 0);
            return psi;
        }
        return null;
    }
}
