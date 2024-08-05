package io.github.xiewuzhiying.vs_addition.fabric.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class VSAdditionFabricMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if(mixinClassName.contains("org.valkyrienskies.mod.fabric.mixin.compat.cc_restitched.MixinSpeakerPosition"))
            return true;
        return false;
    }
}
