package io.github.xiewuzhiying.vs_addition

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours
import dev.architectury.event.events.client.ClientCommandRegistrationEvent
import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.CommandRegistrationEvent
import dev.architectury.event.events.common.EntityEvent
import dev.architectury.event.events.common.InteractionEvent
import dev.architectury.event.events.common.InteractionEvent.RightClickBlock
import dev.architectury.platform.Platform
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.displayLink.ShipDataDisplaySource
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkHandler
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkRenderer
import io.github.xiewuzhiying.vs_addition.compats.vmod.schem.VSAdditionSchemCompat
import io.github.xiewuzhiying.vs_addition.context.EntityFreshCaller
import io.github.xiewuzhiying.vs_addition.context.NonColliderBlockStateProvider
import io.github.xiewuzhiying.vs_addition.context.airpocket.FakeAirPocket
import io.github.xiewuzhiying.vs_addition.context.airpocket.FakeAirPocketClient
import io.github.xiewuzhiying.vs_addition.context.registerCommands
import io.github.xiewuzhiying.vs_addition.networking.VSAdditionMessage
import io.github.xiewuzhiying.vs_addition.networking.airpocket.SyncAllPocketsC2SPacket
import net.spaceeye.vmod.compat.schem.SchemCompatObj
import org.valkyrienskies.core.apigame.VSCore
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.BlockStateInfo

object VSAdditionMod {
    const val MOD_ID = "vs_addition"

    @JvmStatic var CREATE_ACTIVE = false
    @JvmStatic var CREATE_ADDITION_ACTIVE = false
    @JvmStatic var CC_ACTIVE = false
    @JvmStatic var CLOCKWORK_ACTIVE = false
    @JvmStatic var CBC_ACTIVE = false
    @JvmStatic var EUREKA_ACTIVE = false
    @JvmStatic var INTERACTIVE_ACTIVE = false
    @JvmStatic var COMPUTERCRAT_ACTIVE = false
    @JvmStatic var FRAMEDBLOCKS_ACTIVE = false
    @JvmStatic var CBCMW_ACTIVE = false
    @JvmStatic var VMOD_ACTIVE = false



    @JvmStatic
    fun init(core: VSCore) {
        CREATE_ACTIVE = Platform.isModLoaded("create")
        CREATE_ADDITION_ACTIVE = Platform.isModLoaded("create_addition")
        CC_ACTIVE = Platform.isModLoaded("computercraft")
        CLOCKWORK_ACTIVE = Platform.isModLoaded("vs_clockwork")
        CBC_ACTIVE = Platform.isModLoaded("createbigcannons")
        EUREKA_ACTIVE = Platform.isModLoaded("vs_eureka")
        INTERACTIVE_ACTIVE = Platform.isModLoaded("create_interactive")
        COMPUTERCRAT_ACTIVE = Platform.isModLoaded("computercraft")
        FRAMEDBLOCKS_ACTIVE = Platform.isModLoaded("framedblocks")
        CBCMW_ACTIVE = Platform.isModLoaded("cbcmodernwarfare")
        VMOD_ACTIVE = Platform.isModLoaded("valkyrien_mod")

        core.registerConfigLegacy("vs_addition", VSAdditionConfig::class.java)

        VSAdditionMessage.registerC2SPackets()

        BlockStateInfo.init()

        EntityEvent.ADD.register(EntityEvent.Add { entity, world -> EntityFreshCaller.freshEntityInShipyard(entity, world) } )

        CommandRegistrationEvent.EVENT.register { dispatcher, registry, selection ->
            FakeAirPocket.registerCommands(dispatcher, registry, selection)
            registerCommands(dispatcher, registry, selection)
        }

        NonColliderBlockStateProvider.register()

        if (CREATE_ACTIVE) {
            AllDisplayBehaviours.register(
                ShipDataDisplaySource.id,
                ShipDataDisplaySource()
            )
        }

        if (CLOCKWORK_ACTIVE) {
            InteractionEvent.RIGHT_CLICK_BLOCK.register(RightClickBlock { player, hand, pos, face ->
                DualLinkHandler.handler(player, hand, pos, face)
            })
        }

        if (VMOD_ACTIVE) {
            SchemCompatObj.safeAdd("vs_addition") { VSAdditionSchemCompat() }
        }
    }



    @JvmStatic
    fun initClient() {
        VSAdditionMessage.registerS2CPackets()

        if (CLOCKWORK_ACTIVE) {
            ClientTickEvent.CLIENT_POST.register(ClientTickEvent.Client { DualLinkRenderer.tick() })
        }

        ClientCommandRegistrationEvent.EVENT.register { dispatcher, registry ->
            FakeAirPocketClient.registerCommands(dispatcher, registry)
        }

        VSEvents.ShipLoadEventClient.on { event ->
            if (VSAdditionConfig.COMMON.experimental.fakeAirPocket) {
                SyncAllPocketsC2SPacket(event.ship.id).sendToServer()
            }
        }
    }
}