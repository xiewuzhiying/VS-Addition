package io.github.xiewuzhiying.vs_addition.forge.mixin.tacz;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.github.xiewuzhiying.vs_addition.mixinducks.minecraft.HitResultMixinDuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import static io.github.xiewuzhiying.vs_addition.util.ShipUtilsKt.clipIncludeShipsWrapper;

@Mixin(EntityKineticBullet.class)
public abstract class MixinEntityKineticBullet extends Projectile {
    protected MixinEntityKineticBullet(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
            method = "onBulletTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/util/block/BlockRayTrace;rayTraceBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"
            ),
            remap = false
    )
    private BlockHitResult wrap(Level level, ClipContext context, Operation<BlockHitResult> original) {
        return (BlockHitResult) clipIncludeShipsWrapper(level, context, original::call);
    }

    @WrapOperation(
            method = "onBulletTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/entity/EntityKineticBullet;onHitBlock(Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)V"
            ),
            remap = false
    )
    private void getOriginPos(EntityKineticBullet instance, BlockHitResult result, Vec3 fireState, Vec3 offsetPos, Operation<Void> original) {
        if (result instanceof HitResultMixinDuck duck) {
            final Vec3 originPos = duck.vs_addition$getOriginPos();
            if (originPos != null) {
                duck.vs_addition$setLocation(duck.vs_addition$getOriginPos());
                original.call(instance, duck, fireState, duck.vs_addition$getOriginPos());
                return;
            }
        }
        original.call(instance, result, fireState, offsetPos);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;ZLcom/tacz/guns/resource/pojo/data/gun/GunData;Lcom/tacz/guns/resource/pojo/data/gun/BulletData;)V",
            at = @At("RETURN"),
            remap = false
    )
    private void postInit(EntityType<? extends Projectile> type, Level worldIn, LivingEntity throwerIn, ItemStack gunItem, ResourceLocation ammoId, ResourceLocation gunId, ResourceLocation gunDisplayId, boolean isTracerAmmo, GunData gunData, BulletData bulletData, CallbackInfo ci) {
        final Ship ship = VSGameUtilsKt.getShipMountedTo(throwerIn);
        if (ship != null) {
            final Vector3d pos = VectorConversionsMCKt.toJOML(throwerIn.position());
            final Vector3d oPos = new Vector3d(throwerIn.xOld, throwerIn.yOld, throwerIn.zOld);
            ship.getTransform().getWorldToShip().transformPosition(pos);
            ship.getTransform().getWorldToShip().transformPosition(oPos);
            final Vector3d newPos = oPos.add(pos.sub(oPos).mul(0.5));
            this.setPos(newPos.x, newPos.y + throwerIn.getEyeHeight(), newPos.z);
        }
    }
}
