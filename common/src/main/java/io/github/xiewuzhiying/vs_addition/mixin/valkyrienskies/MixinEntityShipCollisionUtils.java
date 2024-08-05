package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import kotlin.Pair;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils;

@Mixin(EntityShipCollisionUtils.class)
public abstract class MixinEntityShipCollisionUtils {
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
}
