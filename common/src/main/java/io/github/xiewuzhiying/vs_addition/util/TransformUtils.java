package io.github.xiewuzhiying.vs_addition.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;

public class TransformUtils {
    public static Quaterniond directionToQuaterniond(Direction facing) {
        return switch (facing) {
            case UP -> new Quaterniond();
            case DOWN -> new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(1.0, 0.0, 0.0)));
            case EAST -> new Quaterniond(new AxisAngle4d(0.5 * Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
            case WEST -> new Quaterniond(new AxisAngle4d(1.5 * Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
            case SOUTH -> new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0))).normalize();
            case NORTH -> new Quaterniond(new AxisAngle4d(Math.PI, new Vector3d(0.0, 1.0, 0.0))).mul(new Quaterniond(new AxisAngle4d(Math.PI / 2.0, new Vector3d(1.0, 0.0, 0.0)))).normalize();
        };
    }

    public static Quaternionf directionToQuaternionf(Direction facing) {
        return new Quaternionf(directionToQuaterniond(facing));
    }

    public static Vec3 toShipyardCoordinates(Ship ship, Vec3 vec3){
        Vector3d vector3d = ship.getWorldToShip().transformPosition(VectorConversionsMCKt.toJOML(vec3));
        return VectorConversionsMCKt.toMinecraft(vector3d);
    }

    public static Vec3i floorToVec3i(Vector3d vec){
        return new Vec3i((int)Math.floor(vec.x), (int)Math.floor(vec.y), (int)Math.floor(vec.z));
    }

    public static Vec3i floorToVec3i(Vec3 vec){
        return new Vec3i((int)Math.floor(vec.x), (int)Math.floor(vec.y), (int)Math.floor(vec.z));
    }

    public static Vec3i floorToVec3i(Vector3f vec){
        return new Vec3i((int)Math.floor(vec.x), (int)Math.floor(vec.y), (int)Math.floor(vec.z));
    }

    public static BlockPos floorToBlockPos(Vector3d vec){
        return new BlockPos(floorToVec3i(vec));
    }

    public static BlockPos floorToBlockPos(Vec3 vec){
        return new BlockPos(floorToVec3i(vec));
    }

    public static BlockPos floorToBlockPos(Vector3f vec){
        return new BlockPos(floorToVec3i(vec));
    }


    public static Vec3 toVec3(BlockPos blockPos){
        return new Vec3(blockPos.getX(),blockPos.getY(),blockPos.getZ());
    }
    public static Vec3 toWorldVec3(Level level, Vec3 vec3){
        Ship ship = VSGameUtilsKt.getShipManagingPos(level,vec3);
        if(ship!=null){
            return VSGameUtilsKt.toWorldCoordinates(ship,vec3);
        }
        return vec3;
    }
    public static Vec3 toWorldVec3(Ship ship,Vec3 vec3){
        if(ship!=null){
            return VSGameUtilsKt.toWorldCoordinates(ship,vec3);
        }
        return vec3;
    }
    public static Vec3 vec3Below(Vec3 vec3,double distance){
        Direction direction = Direction.DOWN;
        return new Vec3(vec3.x + direction.getStepX() * distance, vec3.y + direction.getStepY() * distance, vec3.z + direction.getStepZ() * distance);
    }
    public static Vec3 getFront(Direction direction,BlockPos blockPos){
        return switch (direction) {
            case EAST -> new Vec3(blockPos.getX()+1, blockPos.getY()+0.5, blockPos.getZ()+0.5);
            case SOUTH -> new Vec3(blockPos.getX()+0.5,blockPos.getY()+0.5, blockPos.getZ()+1);
            case WEST -> new Vec3(blockPos.getX(),blockPos.getY()+0.5,blockPos.getZ()+0.5);
            case NORTH -> new Vec3(blockPos.getX()+0.5, blockPos.getY()+0.5,blockPos.getZ());
            case UP -> new Vec3(blockPos.getX()+0.5, blockPos.getY()+1, blockPos.getZ()+0.5);
            default -> new Vec3(blockPos.getX()+0.5, blockPos.getY(), blockPos.getZ()+0.5);

        };
    }

    public static Vec3 getCenterOf(BlockPos blockPos) {
        return new Vec3(blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5);
    }
}