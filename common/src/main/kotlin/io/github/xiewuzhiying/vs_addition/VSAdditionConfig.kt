package io.github.xiewuzhiying.vs_addition

import com.github.imifou.jsonschema.module.addon.annotation.JsonSchema

object VSAdditionConfig {
    @JvmField
    val CLIENT = Client()

    @JvmField
    val SERVER = Server()

    @JvmField
    val COMMON = Common()

    class Client {
        @JsonSchema(description = "Enable mechanical arm item remove unavailable interaction point")
        val enablePointRemoval = true
    }

    class Server {
        @JsonSchema(description = "The force of recoil produced by big cannon fire on a ship")
        val bigCannonRecoilForce: Double = 100000.0

        @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
        val mediumCannonRecoilForce: Double = 21600.0

        @JsonSchema(description = "The force of recoil produced by auto cannon fire on a ship")
        val autoCannonRecoilForce: Double = 800.0

        @JsonSchema(description = "Custom autocannon breech fire rates")
        val customFireRates = listOf(120, 80, 60, 48, 40, 30, 24, 20, 15, 12, 10, 8, 6, 5, 4)

        @JsonSchema(description = "I hope cannons will no longer destroy airplane cockpits")
        val spreadMultiplier : Double = 1.0

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

    class Common {
        @JsonSchema(description = "Disable Create Interactive deployer mixin to use VS Addition's")
        val insteadCreateInteractiveDeployer = true
    }
}