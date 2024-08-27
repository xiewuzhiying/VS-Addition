package io.github.xiewuzhiying.vs_addition.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import org.valkyrienskies.mod.mixin.ValkyrienCommonMixinConfigPlugin;

import java.util.List;

public class VSAdditionCommonMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.contains("org.valkyrienskies.create_interactive.mixin.deployer")) {
            return VSAdditionConfig.COMMON.getInsteadCreateInteractiveDeployer();
        }
        return false;
    }

    private static boolean classExists(final String className) {
        try {
            Class.forName(className, false, ValkyrienCommonMixinConfigPlugin.class.getClassLoader());
            return true;
        } catch (final ClassNotFoundException ex) {
            return false;
        }
    }
}
