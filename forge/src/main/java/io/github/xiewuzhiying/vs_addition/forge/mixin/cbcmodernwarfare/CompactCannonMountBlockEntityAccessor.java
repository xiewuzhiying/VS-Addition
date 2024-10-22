package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity;

@Pseudo
@Mixin(CompactCannonMountBlockEntity.class)
public interface CompactCannonMountBlockEntityAccessor {

    @Invoker(value = "assemble", remap = false)
    void Assemble();

    @Accessor(remap = false)
    float getCannonYaw();

    @Accessor(remap = false)
    float getCannonPitch();
}
