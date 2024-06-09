package io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic;

import com.github.tartaricacid.netmusic.client.audio.NetMusicSound;
import io.github.xiewuzhiying.vs_addition.util.transformUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.net.URL;

@Mixin(NetMusicSound.class)
public abstract class MixinNetMusicSound extends AbstractTickableSoundInstance {
    @Shadow(remap = false) @Final private BlockPos pos;

    @Unique
    private boolean vs_addition$isInShipyard = false;

    protected MixinNetMusicSound(SoundEvent arg, SoundSource arg2, RandomSource arg3) {
        super(arg, arg2, arg3);
    }


    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void set(BlockPos pos, URL songUrl, int timeSecond, CallbackInfo ci) {
        if(VSGameUtilsKt.isBlockInShipyard(Minecraft.getInstance().level, this.pos)) {
            this.vs_addition$isInShipyard = true;
            Vec3 vec = VSGameUtilsKt.toWorldCoordinates(Minecraft.getInstance().level, transformUtils.getCenterOf(this.pos));
            this.x = vec.x;
            this.y = vec.y;
            this.z = vec.z;
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void updatePos(CallbackInfo ci) {
        if(this.vs_addition$isInShipyard){
            Vec3 vec = VSGameUtilsKt.toWorldCoordinates(Minecraft.getInstance().level, transformUtils.getCenterOf(this.pos));
            this.x = vec.x;
            this.y = vec.y;
            this.z = vec.z;
        }
    }
}
