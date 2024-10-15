package io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic.client;

import com.github.tartaricacid.netmusic.network.message.MusicToClientMessage;
import io.github.xiewuzhiying.vs_addition.forge.compats.netmusic.NetMusicSoundOnShip;
import io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic.MusicToClientMessageAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.net.URL;

@Pseudo
@Mixin(MusicToClientMessage.class)
public abstract class MixinMusicToClientMessage {
    @Inject(
            method = "lambda$onHandle$2",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void checkIfOnShip(MusicToClientMessage message, URL url, final CallbackInfoReturnable<SoundInstance> cir) {
        BlockPos pos = ((MusicToClientMessageAccessor)message).getPos();
        final Ship ship = VSGameUtilsKt.getShipManagingPos(Minecraft.getInstance().level, pos.getX(), pos.getY(), pos.getZ());
        if (ship != null) {
            cir.setReturnValue(new NetMusicSoundOnShip(pos, url, ((MusicToClientMessageAccessor)message).getTimeSecond(), ship));
        }
    }
}
