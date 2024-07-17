package io.xiewuzhiying.vs_addition.fabric.compats.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import org.valkyrienskies.core.impl.config.VSConfigClass
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig

class VSAdditionModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            VSClothConfig.createConfigScreenFor(
                parent,
                VSConfigClass.getRegisteredConfig(VSAdditionConfig::class.java)
            )
        }
    }
}