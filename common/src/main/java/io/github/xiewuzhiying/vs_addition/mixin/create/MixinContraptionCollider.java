package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider;

@Mixin(ContraptionCollider.class)
public abstract class MixinContraptionCollider {
    @Inject(
            method = "collideEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private static void setLast(AbstractContraptionEntity contraptionEntity, CallbackInfo ci, @Local(ordinal = 0) Entity entity) {
        Ship ship = VSGameUtilsKt.getShipManagingPos(contraptionEntity.level, contraptionEntity.position());
        if(ship != null)
            ((IEntityDraggingInformationProvider)entity).getDraggingInformation().setLastShipStoodOn(ship.getId());
    }
}
