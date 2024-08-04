package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

@Pseudo
@Mixin(CannonMountBlockEntity.class)
public interface CannonMountBlockEntityAccessor {

    @Invoker(value = "assemble", remap = false)
    void Assemble();

    @Invoker(value = "getMaxDepress", remap = false)
    float GetMaxDepress();

    @Invoker(value = "getMaxElevate", remap = false)
    float GetMaxElevate();

    @Accessor(remap = false)
    float getCannonYaw();

    @Accessor(remap = false)
    float getCannonPitch();
}
