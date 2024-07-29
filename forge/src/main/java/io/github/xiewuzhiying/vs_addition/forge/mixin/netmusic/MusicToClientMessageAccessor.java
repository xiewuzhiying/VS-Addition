package io.github.xiewuzhiying.vs_addition.forge.mixin.netmusic;

import com.github.tartaricacid.netmusic.network.message.MusicToClientMessage;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(MusicToClientMessage.class)
public interface MusicToClientMessageAccessor {
    @Accessor(remap = false)
    BlockPos getPos();

    @Accessor(remap = false)
    int getTimeSecond();
}
