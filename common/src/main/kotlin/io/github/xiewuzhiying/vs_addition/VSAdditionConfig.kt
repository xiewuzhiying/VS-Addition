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

        val embeddium = EMBEDDIUM()

        class EMBEDDIUM {
            @JsonSchema()
            var enableBlockEntityCullOnShips = false
        }

        val experimental = EXPERIMENTAL()

        class EXPERIMENTAL {
            @JsonSchema(description = "Remove under water fog when the camera is in fake air pocket.")
            var removeFogInFakeAirPocket = false

            @JsonSchema(description = "Cull water surface when it's in fake air pocket.")
            var cullWaterSurfaceInFakeAirPocket = true

            @JsonSchema(description = "Cull water surface when it's in fake air pocket.")
            var seaLevel : Double = 62.0

            @JsonSchema(description = "High light fake air pocket aabb.")
            var highLightFakedAirPocket = false

            @JsonSchema(description = "Remove bubble like particles in fake air pocket.")
            var removeBubbleLikeParticlesInFakeAirPocket = false
        }
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

        var defaultFluidDensity : Double = 100.0

        var defaultFluidDragCoefficient : Double = 0.3

        @JsonSchema(description = "Disable some annoying console logs :)")
        var getEntitiesAabbSizeLimit = 100000000.0

        @JsonSchema(description = "Set of non-collider blocks on the ship that are excluded from collision detection.")
        var nonColliderBlocks = setOf(
            "minecraft:oak_leaves",
            "minecraft:spruce_leaves",
            "minecraft:birch_leaves",
            "minecraft:jungle_leaves",
            "minecraft:acacia_leaves",
            "minecraft:dark_oak_leaves",
            "minecraft:cherry_leaves",
            "create:redstone_link"
        )

        var nonColliderBlocksPriority = 127

        val create = CREATE()

        class CREATE {
            @JsonSchema(description = "Compliance of sticker's constraints")
            var stickerCompliance : Double = 1e-10

            @JsonSchema(description = "Max Force of sticker's constraints")
            var stickerMaxForce : Double = 1e10

            @JsonSchema(description = "Enable encased fan to work between ships")
            var encasedFanMixin : Boolean = false

            @JsonSchema(description = "Not available yet. DO NOT OPEN THIS!")
            var copycatMaterialMass : Boolean = false

            val psi = PSI()

            class PSI {
                @JsonSchema(description = "Temporary item storage for each pair of interfaces (unit: slot)", min = 0.0, max = Double.MAX_VALUE)
                var itemTemp : Int = 8

                @JsonSchema(description = "Temporary fluid storage for each pair of interfaces (unit: bucket)", min = 0.0, max = Double.MAX_VALUE)
                var fluidTemp : Int = 4

                @JsonSchema(description = "Temporary energy storage for each pair of interfaces (unit: FE/ME)", min = 0.0, max = Double.MAX_VALUE)
                var energyTemp : Int = 1000000

                @JsonSchema(description = "Max Input every tick of energy interfaces (unit: FE/ME)", min = 0.0, max = Double.MAX_VALUE)
                var energyMaxInput : Int = 5000

                @JsonSchema(description = "Max Output every tick of energy interfaces (unit: FE/ME)", min = 0.0, max = Double.MAX_VALUE)
                var energyMaxOutput : Int = 5000
            }
        }

        val createBigCannons = CREATEBIGCANNONS()

        class CREATEBIGCANNONS {
            @JsonSchema(description = "Adds the ship's velocity vector to the initial velocity of the cannon's projectile")
            var addShipVelocity: Boolean = false

            @JsonSchema(description = "Enable cannon recoil feature")
            var enableCannonRecoil: Boolean = true

            @JsonSchema(description = "The force of recoil produced by big cannon fire on a ship")
            var bigCannonRecoilForce: Double = 100000.0

            @JsonSchema(description = "The force of recoil produced by auto cannon fire on a ship")
            var autoCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "The force of recoil produced by medium cannon fire on a ship")
            var mediumCannonRecoilForce: Double = 21600.0

            @JsonSchema(description = "The force of recoil produced by rotary cannon fire on a ship")
            var rotaryCannonRecoilForce: Double = 800.0

            @JsonSchema(description = "Custom autocannon breech fire rates")
            var customAutoCannonFireRates = listOf(120, 80, 60, 48, 40, 30, 24, 20, 15, 12, 10, 8, 6, 5, 4)
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

        @JsonSchema(description = "Maximum tilt angle (in degrees) at which an entity can stand on a ship.")
        var maxTiltAngle = 45.0

        val create = CREATE()

        class CREATE {
            @JsonSchema(description = "Disable Create Interactive deployer mixin to use VS Addition's")
            var insteadCreateInteractiveDeployer = true
        }

        val clockwork = CLOCKWORK()

        class CLOCKWORK {
            @JsonSchema(description = "Gravitron will disable collision of grabbed ships against the user.")
            var disableGrabbedShipCollision : Boolean = true
        }

        val experimental = EXPERIMENTAL()

        class EXPERIMENTAL {
            @JsonSchema(description = "Enable fake air pocket. This feature may frequently change, potentially causing damage to your old save files. Please consider enabling it with caution.")
            var fakeAirPocket = false
        }
    }
}