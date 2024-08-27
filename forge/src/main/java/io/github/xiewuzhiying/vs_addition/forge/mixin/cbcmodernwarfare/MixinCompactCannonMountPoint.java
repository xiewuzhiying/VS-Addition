package io.github.xiewuzhiying.vs_addition.forge.mixin.cbcmodernwarfare;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountPoint;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMediumcannonContraption;
import riftyboi.cbcmodernwarfare.cannons.medium_cannon.breech.MediumcannonBreechBlockEntity;
import riftyboi.cbcmodernwarfare.index.CBCModernWarfareItem;
import riftyboi.cbcmodernwarfare.munitions.medium_cannon.MediumcannonAmmoItem;
import riftyboi.cbcmodernwarfare.munitions.medium_cannon.MediumcannonCartridgeItem;

@Mixin(CompactCannonMountPoint.class)
public abstract class MixinCompactCannonMountPoint {
    @Inject(
            method = "getInsertedResultAndDoSomething",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void add(ItemStack stack, boolean simulate, AbstractMountedCannonContraption cannon, PitchOrientedContraptionEntity poce, CallbackInfoReturnable<ItemStack> cir) {
        if (cannon instanceof MountedMediumcannonContraption mediumCannon) {
            cir.setReturnValue(mediumcannonInsert(stack, simulate, mediumCannon, poce));
        }
    }

    @Unique
    private static ItemStack mediumcannonInsert(ItemStack stack, boolean simulate, MountedMediumcannonContraption mediumcannon, PitchOrientedContraptionEntity poce) {
        Item item = stack.getItem();
        if (!(item instanceof MediumcannonAmmoItem ammoItem)) {
            return stack;
        }

        BlockEntity blockEntity = mediumcannon.presentBlockEntities.get(mediumcannon.getStartPos());
        if (!(blockEntity instanceof MediumcannonBreechBlockEntity breech)) {
            return stack;
        }

        if (!breech.canBeAutomaticallyLoaded()) {
            return stack;
        }

        if (!(ammoItem instanceof MediumcannonCartridgeItem)) {
            return stack;
        }

        if(breech.getInputBuffer().isEmpty()) {
            if (!simulate) {
                breech.setInputBuffer(stack);
                breech.setLoadingCooldown(getLoadingCooldown());
            }

            ItemStack copy = stack.copy();
            copy.shrink(1);
            return copy;
        }

        Item item2 = breech.getInputBuffer().getItem();
        if (CBCModernWarfareItem.EMPTY_MEDIUMCANNON_CARTRIDGE.get().equals(item2)) {
            if (!simulate) {
                breech.setInputBuffer(stack);
                breech.setLoadingCooldown(getLoadingCooldown());
            }
            if (simulate) {
                stack.setCount(1);
            }
            return new ItemStack(item2);
        }

        return stack;
    }


    @Unique
    private static int getLoadingCooldown() {
        return CBCConfigs.SERVER.cannons.quickfiringBreechLoadingCooldown.get();
    }
}
