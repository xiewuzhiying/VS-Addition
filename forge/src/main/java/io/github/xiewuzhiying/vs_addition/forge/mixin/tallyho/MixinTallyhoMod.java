package io.github.xiewuzhiying.vs_addition.forge.mixin.tallyho;

import edn.stratodonut.tallyho.TallyhoMod;
import edn.stratodonut.tallyho.cbc.CustomHardnessIndex;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(TallyhoMod.class)
public abstract class MixinTallyhoMod {
    @Inject(
            method = "onCommonSetup",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void onCommonSetup(CallbackInfo ci) {
        if (ModList.get().isLoaded("copiescats")) {
            CustomHardnessIndex.init();
        }
        ci.cancel();
    }
}
