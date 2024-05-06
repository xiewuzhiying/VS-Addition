package io.github.xiewuzhiying.vs_addition

import com.github.imifou.jsonschema.module.addon.annotation.JsonSchema

object VSAdditionConfig {
    @JvmField
    val CLIENT = Client()

    @JvmField
    val SERVER = Server()

    class Client

    class Server {
        @JsonSchema(description = "The force of recoil produced by big cannon fire on a ship")
        val bigCannonRecoilForce: Double = 100000.0

        @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
        val mediumCannonRecoilForce: Double = 21600.0

        @JsonSchema(description = "The force of recoil produced by auto cannon fire on a ship")
        val autoCannonRecoilForce: Double = 800.0
    }
}