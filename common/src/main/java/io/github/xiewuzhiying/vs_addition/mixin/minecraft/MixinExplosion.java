package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @Shadow @Final private double x;

    @Shadow @Final private Level level;

    @Shadow @Final private double y;

    @Shadow @Final private double z;

    @WrapOperation(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    public Vec3 applyShipRot(Vec3 instance, double x, double y, double z, Operation<Vec3> original) {
        Ship ship = VSGameUtilsKt.getShipManagingPos(this.level, this.x, this.y, this.z);
        if(ship!=null) {
            Vector3d vec = new Vector3d(x, y, z).rotate(ship.getTransform().getShipToWorldRotation());
            return original.call(instance, vec.x, vec.y, vec.z);
        }
        return original.call(instance, x, y, z);
    }
}
