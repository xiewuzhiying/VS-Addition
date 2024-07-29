package io.github.xiewuzhiying.vs_addition.forge.mixin.create;

import com.simibubi.create.content.contraptions.actors.psi.PortableItemInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.psi.IPSIBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Pseudo
@Mixin(PortableItemInterfaceBlockEntity.class)
public abstract class MixinPortableItemInterfaceBlockEntity extends PortableStorageInterfaceBlockEntity implements IPSIBehavior {


    @Shadow(remap = false) protected LazyOptional<IItemHandlerModifiable> capability;

    @Shadow(remap = false) protected abstract LazyOptional<IItemHandlerModifiable> createEmptyHandler();

    @Unique
    protected ItemStackHandler vs_addition$itemStackHandler;

    public MixinPortableItemInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.vs_addition$itemStackHandler = new ItemStackHandler(0);
    }

    @Override
    public void vs_addition$startTransferringTo(PortableStorageInterfaceBlockEntity pi, float distance) {
        LazyOptional<IItemHandlerModifiable> oldCap = capability;
        this.vs_addition$itemStackHandler = new ItemStackHandler(5);
        capability = LazyOptional.of(() -> this.vs_addition$itemStackHandler);
        ((MixinPortableItemInterfaceBlockEntity)pi).capability = this.capability;
        oldCap.invalidate();
        IPSIBehavior.super.vs_addition$startTransferringTo(pi, distance);
    }

    @Override
    public void vs_addition$stopTransferring() {
        LazyOptional<IItemHandlerModifiable> oldCap = capability;
        capability = createEmptyHandler();
        oldCap.invalidate();
        level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        IPSIBehavior.super.vs_addition$stopTransferring();
    }
}
