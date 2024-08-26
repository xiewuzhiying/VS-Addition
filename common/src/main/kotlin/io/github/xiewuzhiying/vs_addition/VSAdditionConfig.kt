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
        @JsonSchema(description = "VS2 default block mass")
        val defaultBlockMass : Double = 100.0

        @JsonSchema(description = "VS2 default block elasticity")
        val defaultBlockElasticity : Double = 0.3

        @JsonSchema(description = "VS2 default block friction")
        val defaultBlockFriction : Double = 0.5

        @JsonSchema(description = "VS2 default block hardness")
        val defaultBlockHardness : Double = 1.0

        @JsonSchema(description = "Disable some annoying console logs :)")
        val disableSomeWarnings = true

        @JsonSchema(description = "Disable some annoying console logs :)")
        val getEntitiesAabbSizeLimit = 1000.0

        val create = CREATE()

        class CREATE {
            @JsonSchema(description = "Compliance of sticker's constraints")
            val stickerCompliance : Double = 1e-128

            @JsonSchema(description = "Max Force of sticker's constraints")
            val stickerMaxForce : Double = 1e300
        }

        val createBigCannons = CREATEBIGCANNONS()

        class CREATEBIGCANNONS {
            @JsonSchema(description = "The force of recoil produced by big cannon fire on a ship")
            val bigCannonRecoilForce: Double = 100000.0

            @JsonSchema(description = "The force of recoil produced by auto cannon fire on a ship")
            val autoCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
            val mediumCannonRecoilForce: Double = 21600.0

            @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
            val rotaryCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "Custom autocannon breech fire rates")
            val customAutoCannonFireRates = listOf(120, 80, 60, 48, 40, 30, 24, 20, 15, 12, 10, 8, 6, 5, 4)

            @JsonSchema(description = "I hope cannons will no longer destroy airplane cockpits")
            val spreadMultiplier : Double = 1.0
        }

        val clockwork = CLOCKWORK()

        class CLOCKWORK {
            val physBearing = PHYSBEARING()

            class PHYSBEARING {
                @JsonSchema(description = "Compliance of phys bearing's constraints")
                val physBearingCompliance : Double = 1e-10

                @JsonSchema(description = "Max Force of phys bearing's constraints")
                val physBearingMaxForce : Double = 1e10

                @JsonSchema(description = "Omega error multiplier of phys bearing's force")
                val physBearingOmegaErrorMultiplier : Double = 1.0

                @JsonSchema(description = "Angle error multiplier of locked mode phys bearing's force")
                val physBearingAngleErrorMultiplier : Double = 1.0
            }

            @JsonSchema(description = "Multiplier of Clockwork encased fan force")
            val fanForceMultiplier: Double = 1.0
        }

        val computercraft = COMPUTERCRAFT()

        class COMPUTERCRAFT {
            @JsonSchema(description = "Max size of Computer Craft event queue")
            val eventQueueMaxSize: Int = 256

            @JsonSchema(description = "m")
            val defaultMinPeriod: Long = 5L

            @JsonSchema(description = "Enable cheat functions on cannon mount peripheral")
            val enableCheatCannonMountPeripheral = false

            @JsonSchema(description = "Enable cheat functions on flap bearing peripheral")
            val enableCheatFlapBearingPeripheral = false
        }
    }

    class Common {
        @JsonSchema(description = "Disable Create Interactive deployer mixin to use VS Addition's")
        val insteadCreateInteractiveDeployer = true
    }
}