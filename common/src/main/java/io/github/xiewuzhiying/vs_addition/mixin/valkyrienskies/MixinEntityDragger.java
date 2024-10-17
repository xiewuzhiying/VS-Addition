package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.xiewuzhiying.vs_addition.stuff.ExtendedEntityDragger;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.valkyrienskies.mod.common.util.EntityDragger;

@Pseudo
@Mixin(EntityDragger.class)
public abstract class MixinEntityDragger {
    @WrapMethod(
            method = "dragEntitiesWithShips",
            remap = false
    )
    private void replace(Iterable<? extends Entity> entities, Operation<Void> original) {
        ExtendedEntityDragger.dragEntitiesWithShips(entities);
    }
}
