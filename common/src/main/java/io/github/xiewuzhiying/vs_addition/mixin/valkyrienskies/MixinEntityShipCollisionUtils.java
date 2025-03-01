package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.xiewuzhiying.vs_addition.context.EntityShipCollisionDisabler;
import it.unimi.dsi.fastutil.longs.LongSet;
import kotlin.Pair;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils;

import java.util.Iterator;

@Pseudo
@Mixin(EntityShipCollisionUtils.class)
public abstract class MixinEntityShipCollisionUtils {
    @ModifyExpressionValue(
            method = "getShipPolygonsCollidingWithEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/core/api/ships/QueryableShipData;getIntersecting(Lorg/joml/primitives/AABBdc;Ljava/lang/String;)Ljava/lang/Iterable;",
                    remap = false
            )
    )
    private <ShipType extends Ship> Iterable<ShipType> getShipPolygonsCollidingWithEntity(Iterable<ShipType> original, @Local(argsOnly = true) Entity entity) {
        if (entity instanceof EntityShipCollisionDisabler disabler) {
            final Iterator<ShipType> iterator = original.iterator();
            final LongSet exclusions = disabler.getDisabledCollisionBodies();
            while (iterator.hasNext()) {
                final Ship ship = iterator.next();
                if (exclusions.contains(ship.getId())) {
                    iterator.remove();
                }
            }
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "adjustEntityMovementForShipCollisions",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/core/apigame/collision/EntityPolygonCollider;adjustEntityMovementForPolygonCollisions(Lorg/joml/Vector3dc;Lorg/joml/primitives/AABBdc;DLjava/util/List;)Lkotlin/Pair;"
            ),
            remap = false
    )
    private Pair<Vector3dc, Long> setOnGround(Pair<Vector3dc, Long> original, @Local(argsOnly = true) Entity entity) {
        if(original.component2() != null && entity != null)
            entity.setOnGround(true);
        return original;
    }

    /*@WrapOperation(
            method = "getShipPolygonsCollidingWithEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/core/apigame/collision/ConvexPolygonc;getEnclosingAABB(Lorg/joml/primitives/AABBd;)Lorg/joml/primitives/AABBd;"
            ),
            remap = false
    )
    private AABBd check(ConvexPolygonc instance, AABBd aabb, Operation<AABBd> original, @Local(argsOnly = true) Level level) {
        final Ship ship0 = VSGameUtilsKt.getShipManagingPos(level, aabb.minX, aabb.minY, aabb.minZ);
        final Ship ship1 = VSGameUtilsKt.getShipManagingPos(level, aabb.maxX, aabb.maxY, aabb.maxZ);
        if (ship0 != ship1) {
            if (ship0 != null) {
                aabb.setMin(ship0.getTransform().getShipToWorld().transformPosition(aabb.minX, aabb.minY, aabb.minZ, new Vector3d()));
            }
            if (ship1 != null) {
                aabb.setMax(ship1.getTransform().getShipToWorld().transformPosition(aabb.maxX, aabb.maxY, aabb.maxZ, new Vector3d()));
            }
        }
        return aabb;
    }*/
}
