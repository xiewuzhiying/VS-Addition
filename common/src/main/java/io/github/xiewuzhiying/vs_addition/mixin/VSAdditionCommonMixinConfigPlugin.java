package io.github.xiewuzhiying.vs_addition.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VSAdditionCommonMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("io.github.xiewuzhiying.vs_addition.mixin.create.deployer")) {
            return !(VSAdditionMod.getINTERACTIVE_ACTIVE() && !VSAdditionConfig.COMMON.getInsteadCreateInteractiveDeployer());
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
