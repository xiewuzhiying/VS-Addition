package io.github.xiewuzhiying.vs_addition.forge.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class VSAdditionForgeMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if(mixinClassName.contains("org.valkyrienskies.mod.forge.mixin.compat.cc_tweaked.MixinSpeakerPosition"))
            return true;
        if (mixinClassName.contains("riftyboi.cbcmodernwarfare.forge.mixin.CannonMountPointMixin")) {
            return true;
        }
        return false;
    }
}
