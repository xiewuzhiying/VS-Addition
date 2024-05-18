package io.github.xiewuzhiying.vs_addition.util;

import net.minecraft.core.Direction;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class DirectionUtil {
    public static Quaterniond directionToQuaterniond(Direction facing) {
        return switch (facing) {
            case UP -> new Quaterniond().rotateLocalX(Math.toRadians(90));
            case DOWN -> new Quaterniond().rotateLocalX(Math.toRadians(-90));
            case EAST -> new Quaterniond().rotateLocalY(Math.toRadians(90));
            case WEST -> new Quaterniond().rotateLocalY(Math.toRadians(-90));
            case SOUTH -> new Quaterniond();
            case NORTH -> new Quaterniond().rotateLocalY(180);
        };
    }

    public static Quaternionf directionToQuaternionf(Direction facing) {
        return new Quaternionf(directionToQuaterniond(facing));
    }
}
