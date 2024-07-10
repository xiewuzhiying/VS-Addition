package io.github.xiewuzhiying.vs_addition.forge.mixin.embeddiumplus;

import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(EmbyTools.class)
public abstract class MixinEmbyTools {
    @ModifyVariable(
            method = "isEntityInRange(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;II)Z",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true,
            remap = false
    )
    private static Vec3 isInShipyard(Vec3 pos) {
        final Level level = Minecraft.getInstance().level;
        return VSGameUtilsKt.isBlockInShipyard(level, pos) ? VSGameUtilsKt.toWorldCoordinates(level, pos) : pos;
    }
}
