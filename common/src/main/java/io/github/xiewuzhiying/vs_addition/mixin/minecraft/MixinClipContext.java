package io.github.xiewuzhiying.vs_addition.mixin.minecraft;

import io.github.xiewuzhiying.vs_addition.mixinducks.minecraft.ClipContextMixinDuck;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(ClipContext.class)
public abstract class MixinClipContext implements ClipContextMixinDuck {

    @Unique
    private Entity entity;

    @Inject(
            method = "<init>",
            at = @At("RETURN"),
            remap = false
    )
    private void setEntity(Vec3 from, Vec3 _to, ClipContext.Block block, ClipContext.Fluid fluid, Entity entity, CallbackInfo ci) {
        this.entity = entity;
    }

    @Shadow @Final @Mutable private Vec3 from;

    @Shadow @Final @Mutable private Vec3 to;

    @Shadow @Final @Mutable private ClipContext.Block block;

    @Shadow @Final @Mutable private ClipContext.Fluid fluid;

    @Shadow @Final @Mutable private CollisionContext collisionContext;

    @Override
    public void setForm(Vec3 vec3) {
        this.from = vec3;
    }

    @Override
    public void setTo(Vec3 vec3) {
        this.to = vec3;
    }

    @Override
    public ClipContext.Block getBlock() {
        return this.block;
    }

    @Override
    public void setBlock(ClipContext.Block block) {
        this.block = block;
    }

    @Override
    public ClipContext.Fluid getFluid() {
        return this.fluid;
    }

    @Override
    public void setFluid(ClipContext.Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public CollisionContext getCollisionContext() {
        return this.collisionContext;
    }

    @Override
    public void setCollisionContext(CollisionContext ctx) {
        this.collisionContext = ctx;
    }

    @Override
    public void setCollisionContext(Entity entity) {
        this.collisionContext = CollisionContext.of(entity);
    }
}
