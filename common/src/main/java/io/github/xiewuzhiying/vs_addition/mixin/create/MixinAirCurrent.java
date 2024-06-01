package io.github.xiewuzhiying.vs_addition.mixin.create;


import com.bawnorton.mixinsquared.TargetHandler;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AirCurrent.class, priority = 1500)
public class MixinAirCurrent {
    @TargetHandler(
            mixin = "org.valkyrienskies.mod.mixin.mod_compat.create.MixinAirCurrent",
            name = "Lorg/valkyrienskies/mod/mixin/mod_compat/create/MixinAirCurrent;redirectSetDeltaMovement(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V",
            prefix = "redirect" //https://github.com/Bawnorton/MixinSquared/wiki#prefixes
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void cancel(Entity instance, Vec3 motion, @NotNull CallbackInfo ci) {
        instance.setDeltaMovement(motion);
        ci.cancel();
    }
}
