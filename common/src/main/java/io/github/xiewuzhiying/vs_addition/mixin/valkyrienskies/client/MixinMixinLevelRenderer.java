package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.xiewuzhiying.vs_addition.mixinducks.valkyrienskies.ParticleMixinDuck;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.mixin.feature.transform_particles.MixinParticle;

@Mixin(LevelRenderer.class)
public abstract class MixinMixinLevelRenderer {
    @Shadow @Nullable private ClientLevel level;

    /**
     * Render particles in-world. The {@link MixinParticle} is not sufficient because this method includes a distance
     * check, but this mixin is also not sufficient because not every particle is spawned using this method.
     */
    @WrapMethod(
            method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;"
    )
    private Particle spawnParticleInWorld(ParticleOptions options, boolean force, boolean decreased, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Particle> original) {
        final ClientShip ship = VSGameUtilsKt.getShipObjectManagingPos(this.level, (int) x >> 4, (int) z >> 4);

        if (ship == null) {
            // vanilla behaviour
            return original.call(options, force, decreased, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        final Matrix4dc transform = ship.getRenderTransform().getShipToWorldMatrix();

        // in-world position
        final Vector3d p = transform.transformPosition(new Vector3d(x, y, z));

        // in-world velocity
        final Vector3d v = transform
                // Rotate velocity wrt ship transform
                .transformDirection(new Vector3d(xSpeed, ySpeed, zSpeed))
                // Tack on the ships linear velocity (multiplied by 1/20 because particle velocity is given per tick)
                .fma(0.05, ship.getVelocity());

        // Return and re-call this method with new coords

        Particle particle = original.call(options, force, decreased, p.x, p.y, p.z, v.x, v.y, v.z);
        if (particle != null) {
            ((ParticleMixinDuck)particle).vs_addition$setOriginalPosition(new Vector3d(x, y, z));
            ((ParticleMixinDuck)particle).vs_addition$setShip(ship);
            ((ParticleMixinDuck)particle).vs_addition$setFirstTimeScale(ship.getRenderTransform().getShipToWorld().getScale(new Vector3d()).z);
        }
        return particle;
    }
}
