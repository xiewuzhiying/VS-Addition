package io.github.xiewuzhiying.vs_addition.forge.mixin.tacz;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;

@Pseudo
@Mixin(EntityKineticBullet.class)
public abstract class MixinEntityKineticBullet extends Projectile{
    protected MixinEntityKineticBullet(EntityType<? extends Projectile> arg, Level arg2) {
        super(arg, arg2);
    }
    @WrapOperation(
            method = "onBulletTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/util/block/BlockRayTrace;rayTraceBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"
            ),
            remap = false
    )
    private BlockHitResult rayTraceBlocks(Level level, ClipContext context, Operation<BlockHitResult> original) {
        return RaycastUtilsKt.clipIncludeShips(level, context, true, null);
    }
}
