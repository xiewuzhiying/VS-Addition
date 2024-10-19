package io.github.xiewuzhiying.vs_addition.forge.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class VSAdditionForgeMixinCanceller implements MixinCanceller {

    String[] mixinClasses = {
            "edn.stratodonut.tallyho.mixin.MixinMinimap",
            "edn.stratodonut.tallyho.mixin.cbc.MixinCannonMountBlockEntity",
            "edn.stratodonut.tallyho.mixin.cw.MixinBearingController",
            "edn.stratodonut.tallyho.mixin.MixinMixinAbstractContraptionEntity",
            "edn.stratodonut.tallyho.mixin.client.MixinCameraType",
            "org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinDeployTool",
            "org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinSchematicToolBase",
            "org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinSchematicTransformation"
    };

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        for (String mixin : mixinClasses) {
            if (mixinClassName.contains(mixin)) {
                if (mixin.startsWith("org.valkyrienskies.mod.mixin.mod_compat.create.client")) {
                    return ModList.get().isLoaded("tallyho");
                }
                return true;
            }
        }
        return false;
    }
}
