package io.github.xiewuzhiying.vs_addition.fabric.mixin.netmusic.client;

import com.github.tartaricacid.netmusic.networking.message.MusicToClientMessage;
import com.github.tartaricacid.netmusic.receiver.MusicToClientMessageReceiver;
import io.github.xiewuzhiying.vs_addition.fabric.mixin.netmusic.MusicToClientMessageAccessor;
import io.xiewuzhiying.vs_addition.fabric.compats.netmusic.NetMusicSoundOnShip;
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
@Mixin(MusicToClientMessageReceiver.class)
public abstract class MixinMusicToClientMessageReceiver {
    @Inject(
            method = "lambda$receive$0",
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
