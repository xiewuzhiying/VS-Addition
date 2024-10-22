package io.github.xiewuzhiying.vs_addition.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;

import java.util.List;

public class VSAdditionCommonMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.contains("org.valkyrienskies.create_interactive.mixin.deployer")) {
            return VSAdditionConfig.COMMON.getInsteadCreateInteractiveDeployer();
        }
        /*if (mixinClassName.contains("org.valkyrienskies.mod.mixin.feature.explosions.MixinExplosion")) {
            return !(Platform.isModLoaded("lithium") || Platform.isModLoaded("radium") || Platform.isModLoaded("canary"));
        }*/
        if (mixinClassName.contains("org.valkyrienskies.mod.mixin.feature.transform_particles.MixinLevelRenderer")) {
            return true;
        }
        return false;
    }
}
