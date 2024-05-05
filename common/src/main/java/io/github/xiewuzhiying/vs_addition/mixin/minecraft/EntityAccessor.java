package io.github.xiewuzhiying.vs_addition.mixin.minecraft;


import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("random")
    Random getRandom();
}
