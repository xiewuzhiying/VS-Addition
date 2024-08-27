package io.github.xiewuzhiying.vs_addition.fabric.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import org.valkyrienskies.mod.mixin.ValkyrienCommonMixinConfigPlugin;

import java.util.List;

public class VSAdditionFabricMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
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
