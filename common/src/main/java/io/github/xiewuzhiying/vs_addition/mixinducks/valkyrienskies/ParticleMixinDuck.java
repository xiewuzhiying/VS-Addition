package io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies;

import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ClientShip;

public interface ParticleMixinDuck {
    Vector3d vs_addition$getOriginalPosition();

    ClientShip vs_addition$getShip();

    Double vs_addition$getFirstTimeScale();

    void vs_addition$setOriginalPosition(Vector3d position);

    void vs_addition$setShip(ClientShip ship);

    void vs_addition$setFirstTimeScale(double scale);
}
