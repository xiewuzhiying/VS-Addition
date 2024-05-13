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

        @JsonSchema(description = "Enable cheat functions on cannon mount peripherals")
        val enableCheatCannonMountPeripheral = false

        @JsonSchema(description = "Enable cheat functions on flap bearing peripherals")
        val enableCheatFlapBearingPeripheral = false

        @JsonSchema(description = "Compliance of sticker's constraints")
        val stickerCompliance : Double = 1e-128

        @JsonSchema(description = "Max Force of sticker's constraints")
        val stickerMaxForce : Double = 1e300

        @JsonSchema(description = "VS2 default block mass")
        val defaultBlockMass : Double = 100.0
    }
}