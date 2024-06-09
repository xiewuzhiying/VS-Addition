package io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic;

import com.github.tartaricacid.netmusic.client.audio.NetMusicSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.net.URL;

@Mixin(NetMusicSound.class)
public abstract class MixinNetMusicSound extends AbstractTickableSoundInstance {
    protected MixinNetMusicSound(SoundEvent arg, SoundSource arg2) {
        super(arg, arg2);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void set(BlockPos pos, URL songUrl, int timeSecond, CallbackInfo ci) {
        Vector3d vec = VSGameUtilsKt.toWorldCoordinates(Minecraft.getInstance().level, this.x, this.y, this.z);
        this.x = vec.x;
        this.y = vec.z;
        this.z = vec.z;
    }
}
