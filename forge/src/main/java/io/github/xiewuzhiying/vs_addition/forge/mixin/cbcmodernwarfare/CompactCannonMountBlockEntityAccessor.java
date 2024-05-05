package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity;

@Mixin(CompactCannonMountBlockEntity.class)
public interface CompactCannonMountBlockEntityAccessor {

    @Invoker("assemble")
    void Assemble();

    @Invoker("getMaxDepress")
    float GetMaxDepress();

    @Invoker("getMaxElevate")
    float GetMaxElevate();
}
