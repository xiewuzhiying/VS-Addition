package io.github.xiewuzhiying.vs_addition.mixin.createbigcannons;

import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

@Mixin(CannonMountBlockEntity.class)
public interface CannonMountBlockEntityAccessor {

    @Invoker("assemble")
    void Assemble();

    @Invoker("getMaxDepress")
    float GetMaxDepress();

    @Invoker("getMaxElevate")
    float GetMaxElevate();

    @Accessor
    float getCannonYaw();

    @Accessor
    float getCannonPitch();
}
