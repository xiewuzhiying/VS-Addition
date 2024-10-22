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
        var enablePointRemoval = true
    }

    class Server {
        @JsonSchema(description = "VS2 default block mass")
        var defaultBlockMass : Double = 100.0

        @JsonSchema(description = "VS2 default block elasticity")
        var defaultBlockElasticity : Double = 0.3

        @JsonSchema(description = "VS2 default block friction")
        var defaultBlockFriction : Double = 0.5

        @JsonSchema(description = "VS2 default block hardness")
        var defaultBlockHardness : Double = 1.0

        @JsonSchema(description = "Disable some annoying console logs :)")
        var disableSomeWarnings = true

        @JsonSchema(description = "Disable some annoying console logs :)")
        var getEntitiesAabbSizeLimit = 1000.0

        val create = CREATE()

        class CREATE {
            @JsonSchema(description = "Compliance of sticker's constraints")
            var stickerCompliance : Double = 1e-128

            @JsonSchema(description = "Max Force of sticker's constraints")
            var stickerMaxForce : Double = 1e300

            @JsonSchema(description = "Enable encased fan to work between ships")
            var encasedFanMixin : Boolean = false
        }

        val createBigCannons = CREATEBIGCANNONS()

        class CREATEBIGCANNONS {
            @JsonSchema(description = "The force of recoil produced by big cannon fire on a ship")
            var bigCannonRecoilForce: Double = 100000.0

            @JsonSchema(description = "The force of recoil produced by auto cannon fire on a ship")
            var autoCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
            var mediumCannonRecoilForce: Double = 21600.0

            @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
            var rotaryCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "Custom autocannon breech fire rates")
            var customAutoCannonFireRates = listOf(120, 80, 60, 48, 40, 30, 24, 20, 15, 12, 10, 8, 6, 5, 4)

            @JsonSchema(description = "I hope cannons will no longer destroy airplane cockpits")
            var spreadMultiplier : Double = 1.0
        }

        val clockwork = CLOCKWORK()

        class CLOCKWORK {
            val physBearing = PHYSBEARING()

            class PHYSBEARING {
                @JsonSchema(description = "Compliance of phys bearing's constraints")
                var physBearingCompliance : Double = 1e-10

                @JsonSchema(description = "Max Force of phys bearing's constraints")
                var physBearingMaxForce : Double = 1e10

                @JsonSchema(description = "Omega error multiplier of phys bearing's force")
                var physBearingOmegaErrorMultiplier : Double = 1.0

                @JsonSchema(description = "Angle error multiplier of locked mode phys bearing's force")
                var physBearingAngleErrorMultiplier : Double = 1.0
            }

            @JsonSchema(description = "Multiplier of Clockwork encased fan force")
            var fanForceMultiplier: Double = 1.0
        }

        val computercraft = COMPUTERCRAFT()

        class COMPUTERCRAFT {
            @JsonSchema(description = "Max size of Computer Craft event queue")
            var eventQueueMaxSize: Int = 256

            @JsonSchema(description = "m")
            var defaultMinPeriod: Long = 5L

            @JsonSchema(description = "Enable cheat functions on cannon mount peripheral")
            var enableCheatCannonMountPeripheral = false

            @JsonSchema(description = "Enable cheat functions on flap bearing peripheral")
            var enableCheatFlapBearingPeripheral = false
        }

        val experimental = EXPERIMENTAL()

        class EXPERIMENTAL {
            @JsonSchema(description = "Enable another explosion for ships. (Needs reboot & may have an impact on performance)")
            var explosion = false
        }
    }

    class Common {
        @JsonSchema(description = "Disable Create Interactive deployer mixin to use VS Addition's")
        var insteadCreateInteractiveDeployer = true
    }
}