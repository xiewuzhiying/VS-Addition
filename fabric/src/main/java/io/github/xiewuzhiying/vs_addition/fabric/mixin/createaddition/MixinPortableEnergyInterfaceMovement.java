package io.github.xiewuzhiying.vs_addition.fabric.mixin.createaddition;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyInterfaceBlock;
import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyInterfaceBlockEntity;
import com.mrh0.createaddition.blocks.portable_energy_interface.PortableEnergyInterfaceMovement;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Mixin(PortableEnergyInterfaceMovement.class)
public abstract class MixinPortableEnergyInterfaceMovement implements MovementBehaviour {

    @ModifyExpressionValue(
            method = "findInterface",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 getCenterOf(Vec3 original, @Local MovementContext context) {
        Vec3 transfromedPos = VSGameUtilsKt.toWorldCoordinates(context.world, original);
        Ship ship = VSGameUtilsKt.getShipManagingPos(context.world, context.position);
        if(ship!=null)
            transfromedPos = VectorConversionsMCKt.toMinecraft(ship.getTransform().getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(transfromedPos)));
        return transfromedPos;
    }

    @Redirect(
            method = "findInterface",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/blocks/portable_energy_interface/PortableEnergyInterfaceMovement;findStationaryInterface(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lcom/mrh0/createaddition/blocks/portable_energy_interface/PortableEnergyInterfaceBlockEntity;"
            )
    )
    public PortableEnergyInterfaceBlockEntity findStationaryInterface(PortableEnergyInterfaceMovement instance, Level level, BlockPos blockPos, BlockState world, Direction pos, @Local MovementContext context) {
        Ship selfShip = VSGameUtilsKt.getShipManagingPos(level, blockPos);
        Vector3d selfDirectionVec = VectorConversionsMCKt.toJOML(context.rotation.apply(Vec3.atLowerCornerOf(context.state
                .getValue(PortableEnergyInterfaceBlock.FACING).getNormal())));

        for (int i = 0; i < 2; i++) {

            Vector3d checkPos = VSGameUtilsKt.toWorldCoordinates(level, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(0.5, 0.5, 0.5));
            if (i!=0){
                checkPos = checkPos.add(selfDirectionVec.mul(i-0.125));
            }
            List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, checkPos.x, checkPos.y, checkPos.z, 2);
            ships.add(VSGameUtilsKt.toWorldCoordinates(level, checkPos));

            for(Vector3d eachShipPos : ships) {
                PortableEnergyInterfaceBlockEntity psi = findPSI(level, eachShipPos);
                if (psi != null) {
                    Vector3d selfVec = selfDirectionVec.normalize().negate();
                    if (selfShip!=null)
                        selfVec = selfVec.rotate(selfShip.getTransform().getShipToWorldRotation());
                    Vector3d directionVec = VectorConversionsMCKt.toJOML(Vec3.atLowerCornerOf(psi.getBlockState()
                            .getValue(PortableStorageInterfaceBlock.FACING).getNormal())).normalize();
                    Ship ship = VSGameUtilsKt.getShipManagingPos(level, eachShipPos);
                    if (ship!=null)
                        directionVec = directionVec.rotate(ship.getTransform().getShipToWorldRotation());
                    if (Math.toDegrees(Math.acos(new Vector3d(directionVec).dot(new Vector3d(selfVec))))  <= 22.5)
                        return psi;
                }
            }
        }
        return null;
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;closerThan(Lnet/minecraft/core/Position;D)Z"
            )
    )
    public boolean closerThan(Vec3 vec, Position pos, double distance, @Local MovementContext context) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec.x, vec.y, vec.z, pos.x(), pos.y(), pos.z()) < distance * distance;
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;distanceTo(Lnet/minecraft/world/phys/Vec3;)D"
            )
    )
    public double distanceTo(Vec3 vec1, Vec3 vec2, @Local MovementContext context) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(context.world, vec1.x, vec1.y, vec1.z, vec2.x(), vec2.y(), vec2.z());
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/blocks/portable_energy_interface/PortableEnergyInterfaceMovement;getStationaryInterfaceAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lcom/mrh0/createaddition/blocks/portable_energy_interface/PortableEnergyInterfaceBlockEntity;"
            )
    )
    public PortableEnergyInterfaceBlockEntity redirectToFindPSI(PortableEnergyInterfaceMovement instance, Level level, BlockPos pos, BlockState state, Direction direction) {
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
    public PortableEnergyInterfaceBlockEntity findPSI(Level level, Vector3d pos) {
        BlockPos checkThis = new BlockPos(TransformUtils.floorToBlockPos(pos));
        if(level.getBlockEntity(checkThis) instanceof PortableEnergyInterfaceBlockEntity psi) {
            if(psi.isPowered())
                return null;
            return psi;
        }
        return null;
    }
}
