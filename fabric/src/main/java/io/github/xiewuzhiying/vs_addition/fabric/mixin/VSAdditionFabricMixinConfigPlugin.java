package io.github.xiewuzhiying.vs_addition.fabric.mixin;

import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import io.github.xiewuzhiying.vs_addition.mixin.VSAdditionCommonMixinCanceller;
import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;

import java.util.List;
import java.util.Set;

public class VSAdditionFabricMixinConfigPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void onLoad(String mixinPackage) {
        super.onLoad(mixinPackage);
        MixinCancellerRegistrar.register(new VSAdditionCommonMixinCanceller());
        MixinCancellerRegistrar.register(new VSAdditionFabricMixinCanceller());
    }
}
