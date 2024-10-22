package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies.client;

import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ParticleMixinDuck;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(Particle.class)
public abstract class MixinParticle implements ParticleMixinDuck {

    @Shadow @Final protected ClientLevel level;
    @Unique
    private Vector3d vs_addition$originalPosition = null;

    @Unique
    private ClientShip vs_addition$ship = null;

    @Unique
    private Double vs_addition$FirstTimeScale = null;

    @Inject(
            method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDD)V",
            at = @At("TAIL"),
            remap = false
    )
    private void checkShipCoords(final ClientLevel world, final double x, final double y, final double z,
                                 final CallbackInfo ci) {
        this.vs_addition$originalPosition = new Vector3d(x, y, z);
        this.vs_addition$ship = VSGameUtilsKt.getShipObjectManagingPos(this.level, this.vs_addition$originalPosition);
        if (this.vs_addition$ship != null) {
            this.vs_addition$FirstTimeScale = this.vs_addition$ship.getRenderTransform().getShipToWorld().getScale(new Vector3d()).z;
        }
    }

    @Inject(
            method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)V",
            at = @At("TAIL")
    )
    private void checkShipPosAndVelocity(final ClientLevel world, final double x, final double y, final double z,
                                         final double velocityX,
                                         final double velocityY, final double velocityZ, final CallbackInfo ci) {
        this.vs_addition$originalPosition = new Vector3d(x, y, z);
        this.vs_addition$ship = VSGameUtilsKt.getShipObjectManagingPos(this.level, this.vs_addition$originalPosition);
        if (this.vs_addition$ship != null) {
            this.vs_addition$FirstTimeScale = this.vs_addition$ship.getRenderTransform().getShipToWorld().getScale(new Vector3d()).z;
        }
    }

    @Override
    public Vector3d vs_addition$getOriginalPosition() {
        return this.vs_addition$originalPosition;
    }

    @Override
    public ClientShip vs_addition$getShip() {
        return this.vs_addition$ship;
    }

    @Override
    public Double vs_addition$getFirstTimeScale() {
        return this.vs_addition$FirstTimeScale;
    }

    @Override
    public void vs_addition$setOriginalPosition(Vector3d position) {
        this.vs_addition$originalPosition = position;
    }

    @Override
    public void vs_addition$setShip(ClientShip ship) {
        this.vs_addition$ship = ship;
    }

    @Override
    public void vs_addition$setFirstTimeScale(double scale) {
        this.vs_addition$FirstTimeScale = scale;
    }
}
