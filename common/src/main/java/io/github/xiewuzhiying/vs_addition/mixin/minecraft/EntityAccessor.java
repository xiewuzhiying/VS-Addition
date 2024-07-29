package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Pseudo
@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor
    Random getRandom();
}
