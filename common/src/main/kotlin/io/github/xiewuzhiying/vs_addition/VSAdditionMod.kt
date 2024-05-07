package io.github.xiewuzhiying.vs_addition

import dev.architectury.platform.Platform
import org.valkyrienskies.core.impl.config.VSConfigClass

object VSAdditionMod {
    const val MOD_ID = "vs_addition"

    @JvmStatic var CREATE_ACTIVE = false
    @JvmStatic var CC_ACTIVE = false
    @JvmStatic var CLOCKWORK_ACTIVE = false
    @JvmStatic var CBC_ACTIVE = false
    @JvmStatic var EUREKA_ACTIVE = false

    @JvmStatic
    fun init() {
        CREATE_ACTIVE = Platform.isModLoaded("create")
        CC_ACTIVE = Platform.isModLoaded("computercraft")
        CLOCKWORK_ACTIVE = Platform.isModLoaded("vs_clockwork")
        CBC_ACTIVE = Platform.isModLoaded("createbigcannons")
        EUREKA_ACTIVE = Platform.isModLoaded("vs_eureka")

        VSConfigClass.registerConfig("vs_addition", VSAdditionConfig::class.java)
    }

    @JvmStatic
    fun initClient() {
    }
}