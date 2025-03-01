package io.github.xiewuzhiying.vs_addition.fabric.compats.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import org.valkyrienskies.core.apigame.VSCoreFactory
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig


class VSAdditionModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            VSClothConfig.createConfigScreenFor(
                parent,
                VSAdditionConfig::class.java
            )
        }
    }
}