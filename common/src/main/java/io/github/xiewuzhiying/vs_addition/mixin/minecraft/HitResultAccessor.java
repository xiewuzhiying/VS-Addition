package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HitResult.class)
public interface HitResultAccessor {
    @Accessor
    void setLocation(Vec3 vec3);
}
