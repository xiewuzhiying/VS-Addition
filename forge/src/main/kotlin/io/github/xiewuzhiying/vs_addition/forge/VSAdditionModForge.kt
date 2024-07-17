package io.github.xiewuzhiying.vs_addition.forge

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours
import dan200.computercraft.api.ComputerCraftAPI
import dev.architectury.platform.forge.EventBuses
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.init
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient
import io.github.xiewuzhiying.vs_addition.compats.create.behaviour.link.DualLinkRenderer
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.ForgePeripheralProvider
import io.github.xiewuzhiying.vs_addition.forge.compats.create.behaviour.link.DualLinkHandler
import io.github.xiewuzhiying.vs_addition.forge.content.redstone.display_link.target.FramedSignDisplayTarget
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.ConfigGuiHandler
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.valkyrienskies.core.impl.config.VSConfigClass
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import xfacthd.framedblocks.common.FBContent

@Mod(VSAdditionMod.MOD_ID)
class VSAdditionModForge {

    init {
        CREATE_ACTIVE = ModList.get().isLoaded("create")
        CC_ACTIVE = ModList.get().isLoaded("computercraft")
        FRAMEDBLOCKS_ACTIVE = ModList.get().isLoaded("framedblocks")
        CBCMW_ACTIVE = ModList.get().isLoaded("cbcmodernwarfare")

        getModBus().addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }
        getModBus().addListener { event: FMLCommonSetupEvent? ->
            serverSetup(
                event
            )
        }

        EventBuses.registerModEventBus(VSAdditionMod.MOD_ID, MOD_CONTEXT.getKEventBus())

        init()

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory::class.java) {
            ConfigGuiHandler.ConfigGuiFactory { _, parent ->
                VSClothConfig.createConfigScreenFor(
                    parent,
                    VSConfigClass.getRegisteredConfig(VSAdditionConfig::class.java)
                )
            }
        }

        getForgeBus().addListener { event: RightClickBlock? ->
            rightClickBlock(
                event
            )
        }
        getForgeBus().addListener { event: ClientTickEvent? ->
            clientTick(event)
        }
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }

    private fun serverSetup(event: FMLCommonSetupEvent?) {
        if(FRAMEDBLOCKS_ACTIVE && CREATE_ACTIVE)
            AllDisplayBehaviours.assignBlockEntity(
                AllDisplayBehaviours.register(
                    ResourceLocation(
                        VSAdditionMod.MOD_ID,
                        "framed_sign_display_target"
                    ), FramedSignDisplayTarget()
                ), FBContent.blockEntityTypeFramedSign.get()
            )

        if(CC_ACTIVE)
            ComputerCraftAPI.registerPeripheralProvider(ForgePeripheralProvider())
    }

    private fun rightClickBlock(event: RightClickBlock?) {
        if(event==null)
            return
        if(CREATE_ACTIVE)
            DualLinkHandler.onBlockActivated(event)
    }

    private fun clientTick(event: ClientTickEvent?) {
        if(event==null)
            return
        if(CREATE_ACTIVE)
            DualLinkRenderer.tick()
    }

    companion object {
        fun getModBus(): IEventBus = MOD_BUS
        fun getForgeBus(): IEventBus = FORGE_BUS
        var CREATE_ACTIVE = false
        var CC_ACTIVE = false
        var FRAMEDBLOCKS_ACTIVE = false
        var CBCMW_ACTIVE = false
    }

}
