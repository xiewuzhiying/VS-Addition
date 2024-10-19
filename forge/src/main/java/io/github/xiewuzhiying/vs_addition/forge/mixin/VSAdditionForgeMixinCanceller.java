package io.github.xiewuzhiying.vs_addition.forge.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class VSAdditionForgeMixinCanceller implements MixinCanceller {

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.contains("edn.stratodonut.tallyho.mixin.MixinMinimap") ||
                mixinClassName.contains("edn.stratodonut.tallyho.mixin.cbc.MixinCannonMountBlockEntity") ||
                mixinClassName.contains("edn.stratodonut.tallyho.mixin.cw.MixinBearingController") ||
                mixinClassName.contains("edn.stratodonut.tallyho.mixin.MixinMixinAbstractContraptionEntity") ||
                mixinClassName.contains("edn.stratodonut.tallyho.mixin.client.MixinCameraType")
        ) {
            return true;
        }
        if (mixinClassName.contains("org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinDeployTool") ||
                mixinClassName.contains("org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinSchematicToolBase") ||
                mixinClassName.contains("org.valkyrienskies.mod.mixin.mod_compat.create.client.MixinSchematicTransformation")) {
            return ModList.get().isLoaded("tallyho");
        }
        return false;
    }
}
