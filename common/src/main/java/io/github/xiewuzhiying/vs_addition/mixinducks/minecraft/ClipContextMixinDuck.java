package io.github.xiewuzhiying.vs_addition.mixinducks.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public interface ClipContextMixinDuck {
    void setForm(Vec3 vec3);

    void setTo(Vec3 vec3);

    ClipContext.Block getBlock();

    void setBlock(ClipContext.Block block);

    ClipContext.Fluid getFluid();

    void setFluid(ClipContext.Fluid fluid);

    Entity getEntity();

    void setEntity(Entity entity);

    CollisionContext getCollisionContext();

    void setCollisionContext(CollisionContext ctx);

    void setCollisionContext(Entity entity);
}
