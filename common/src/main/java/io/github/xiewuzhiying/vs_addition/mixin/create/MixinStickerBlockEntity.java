package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.contraptions.chassis.StickerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.compats.create.content.contraptions.chassis.StickerWithShipHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(StickerBlockEntity.class)
public abstract class MixinStickerBlockEntity extends SmartBlockEntity {

    @Shadow public abstract boolean isBlockStateExtended();

    @Unique
    private StickerWithShipHandler vs_addition$handler = null;

    public MixinStickerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(BlockEntityType<? extends StickerBlockEntity> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        this.vs_addition$handler = new StickerWithShipHandler(this.level, this.getBlockPos(), this::vs_addition$playClientSound);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    private void tick(CallbackInfo ci) {
        this.vs_addition$handler.tick(isBlockStateExtended());
    }

    @Override
    public void remove() {
        this.vs_addition$handler = null;
        super.remove();
    }


    /**
     * Safe wrapper for 'playSound', which is removed at runtime on fabric servers.
     */
    @Unique
    public Void vs_addition$playClientSound(boolean attach) {
        if(this.level.isClientSide) {
            playSound(attach);
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    @Shadow(remap = false)
    public abstract void playSound(boolean attach);
}

