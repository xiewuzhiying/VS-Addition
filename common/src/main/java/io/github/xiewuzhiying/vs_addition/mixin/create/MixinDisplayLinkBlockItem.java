package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockItem;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.mod.common.VSGameUtilsKt;


@Mixin(DisplayLinkBlockItem.class)
public abstract class MixinDisplayLinkBlockItem extends BlockItem {

    public MixinDisplayLinkBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Unique
    private Level vs_addition$accessedLevel;

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = pContext.getPlayer();
        vs_addition$accessedLevel = level;

        if (player == null)
            return InteractionResult.FAIL;

        if (player.isSteppingCarefully() && stack.hasTag()) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;
            player.displayClientMessage(Lang.translateDirect("display_link.clear"), true);
            stack.setTag(null);
            return InteractionResult.SUCCESS;
        }

        if (!stack.hasTag()) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;
            CompoundTag stackTag = stack.getOrCreateTag();
            stackTag.put("SelectedPos", NbtUtils.writeBlockPos(pos));
            player.displayClientMessage(Lang.translateDirect("display_link.set"), true);
            stack.setTag(stackTag);
            return InteractionResult.SUCCESS;
        }

        CompoundTag tag = stack.getTag();
        CompoundTag teTag = new CompoundTag();

        BlockPos selectedPos = NbtUtils.readBlockPos(tag.getCompound("SelectedPos"));
        BlockPos placedPos = pos.relative(pContext.getClickedFace(), state.canBeReplaced() ? 0 : 1);

        if (!selectedPos.closerThan(placedPos, AllConfigs.server().logistics.displayLinkRange.get())) {
            player.displayClientMessage(Lang.translateDirect("display_link.too_far")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        teTag.put("TargetOffset", NbtUtils.writeBlockPos(selectedPos.subtract(placedPos)));
        tag.put("BlockEntityTag", teTag);

        InteractionResult useOn = super.useOn(pContext);
        if (level.isClientSide || useOn == InteractionResult.FAIL)
            return useOn;

        ItemStack itemInHand = player.getItemInHand(pContext.getHand());
        if (!itemInHand.isEmpty())
            itemInHand.setTag(null);
        player.displayClientMessage(Lang.translateDirect("display_link.success")
                .withStyle(ChatFormatting.GREEN), true);
        return useOn;
    }


    @Redirect(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            )
    )
    public boolean closerThan(BlockPos instance, Vec3i pVector, double pDistance) {
        return VSGameUtilsKt.squaredDistanceBetweenInclShips(vs_addition$accessedLevel, instance.getX(), instance.getY(), instance.getZ(), pVector.getX(), pVector.getY(), pVector.getZ()) < Mth.square(pDistance);
    }
}
