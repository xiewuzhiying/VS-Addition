package io.github.xiewuzhiying.vs_addition.mixin.create.client.steamWhistle;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import com.simibubi.create.content.decoration.steamWhistle.WhistleSoundInstance;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.compats.create.content.decoration.steamWhistle.WhistleSoundInstanceOnShip;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WhistleBlockEntity.class)
public abstract class MixinWhistleBlockEntity extends SmartBlockEntity {
    @Shadow(remap = false) protected WhistleSoundInstance soundInstance;

    public MixinWhistleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ModifyArg(
            method = "tickAudio",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"
            )
    )
    private SoundInstance onShip(SoundInstance sound, @Local(argsOnly = true) WhistleBlock.WhistleSize size) {
        Ship ship = VSGameUtilsKt.getShipManagingPos(this.level, this.worldPosition);
        if (ship != null) {
            this.soundInstance = new WhistleSoundInstanceOnShip(size, this.worldPosition, ship);
            return this.soundInstance;
        } else {
            return sound;
        }
    }
}
