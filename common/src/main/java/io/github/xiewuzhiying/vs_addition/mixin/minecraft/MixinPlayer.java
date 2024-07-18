package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean noShipCollision(Level level, AABB aabb) {
        return !VSGameUtilsKt.getShipsIntersecting(level, aabb).iterator().hasNext();
    }

    @WrapOperation(
            method = "maybeBackOffFromEdge",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
                    ordinal = 0
            )
    )
    private boolean checkShipCollision1(Level level, Entity entity, AABB aabb, Operation<Boolean> original) {
        return original.call(level, entity, aabb) && noShipCollision(level, aabb);
    }

    @WrapOperation(
            method = "maybeBackOffFromEdge",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
                    ordinal = 1
            )
    )
    private boolean checkShipCollision2(Level level, Entity entity, AABB aabb, Operation<Boolean> original) {
        return original.call(level, entity, aabb) && noShipCollision(level, aabb);
    }

    @WrapOperation(
            method = "maybeBackOffFromEdge",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
                    ordinal = 2
            )
    )
    private boolean checkShipCollision3(Level level, Entity entity, AABB aabb, Operation<Boolean> original) {
        return original.call(level, entity, aabb) && noShipCollision(level, aabb);
    }

    @WrapOperation(
            method = "isAboveGround",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z",
                    ordinal = 0
            )
    )
    private boolean checkShipCollision4(Level level, Entity entity, AABB aabb, Operation<Boolean> original) {
        return original.call(level, entity, aabb) && noShipCollision(level, aabb);
    }
}
