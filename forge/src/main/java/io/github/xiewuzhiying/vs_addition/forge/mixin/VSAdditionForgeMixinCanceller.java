package io.github.xiewuzhiying.vs_addition.forge.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import org.valkyrienskies.mod.mixin.ValkyrienCommonMixinConfigPlugin;

import java.util.List;

public class VSAdditionForgeMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.contains("riftyboi.cbcmodernwarfare.forge.mixin.CannonMountPointMixin")) {
            return true;
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
