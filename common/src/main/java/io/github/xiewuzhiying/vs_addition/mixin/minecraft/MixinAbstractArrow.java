package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider;

import java.util.function.Consumer;

@Pseudo
@Mixin(AbstractArrow.class)
public abstract class MixinAbstractArrow extends Entity implements IEntityDraggingInformationProvider {
    public MixinAbstractArrow(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
            method = "shouldFall",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/phys/AABB;)Z"
            )
    )
    public boolean shipCollision(Level level, AABB aabb, Operation<Boolean> original) {
        final boolean[] noShipCollision = {true};
        Consumer<AABB> consumer = aabb1 -> {
            if (noShipCollision[0]) {
                noShipCollision[0] = level.noCollision(aabb1);
                Ship ship = VSGameUtilsKt.getShipManagingPos(this.level, aabb1.getCenter());
                if(ship!=null)
                    this.getDraggingInformation().setLastShipStoodOn(ship.getId());
            }
        };
        VSGameUtilsKt.transformFromWorldToNearbyShipsAndWorld(level, aabb, consumer);
        return original.call(level, aabb) && noShipCollision[0];
    }
}
