package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;

@Pseudo
@Mixin(AbstractAutocannonBreechBlockEntity.class)
public interface AbstractAutocannonBreechBlockEntityAccessor {
    @Accessor(remap = false)
    int getFireRate();

    @Accessor(remap = false)
    void setFiringCooldown(int i);

    @Accessor(remap = false)
    void setAnimateTicks(int i);
}
