package io.github.xiewuzhiying.vs_addition.mixin.minecraft;


import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("random")
    RandomSource getRandom();

    @Accessor("level")
    Level getLevel();
}
