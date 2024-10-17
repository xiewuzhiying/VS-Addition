package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountPoint;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMediumcannonContraption;
import riftyboi.cbcmodernwarfare.cannons.medium_cannon.breech.MediumcannonBreechBlockEntity;
import riftyboi.cbcmodernwarfare.index.CBCModernWarfareItem;

@Pseudo
@Restriction(
        conflict = {
                @Condition(value = "cbcmodernwarfare", versionPredicates = "0.0.5f+mc.1.20.1-forge")
        }
)
@Mixin(CompactCannonMountPoint.class)
public abstract class MixinCompactCannonMountPoint {
    @Shadow(remap = false)
    private static int getLoadingCooldown() {
        throw new AssertionError();
    }

    @Inject(
            method = "mediumcannonInsert",
            at = @At(
                    value = "INVOKE",
                    target = "Lriftyboi/cbcmodernwarfare/cannons/medium_cannon/breech/MediumcannonBreechBlockEntity;canBeAutomaticallyLoaded()Z",
                    shift = At.Shift.AFTER
            ),
            cancellable = true,
            remap = false
    )
    private static void mediumcannonInsert(ItemStack stack, boolean simulate, MountedMediumcannonContraption mediumcannon, PitchOrientedContraptionEntity poce, CallbackInfoReturnable<ItemStack> cir, @Local MediumcannonBreechBlockEntity breech) {
        Item item2 = breech.getInputBuffer().getItem();
        if (CBCModernWarfareItem.EMPTY_MEDIUMCANNON_CARTRIDGE.get().equals(item2)) {
            if (!simulate) {
                breech.setInputBuffer(stack);
                breech.setLoadingCooldown(getLoadingCooldown());
            }
            stack.setCount(1);
            cir.setReturnValue(new ItemStack(item2));
        }
    }

}
