package io.github.xiewuzhiying.vs_addition.forge

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours
import dan200.computercraft.impl.Peripherals
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.init
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient
import io.github.xiewuzhiying.vs_addition.forge.compat.computercraft.ForgePeripheralProvider
import io.github.xiewuzhiying.vs_addition.forge.content.redstone.displayLink.target.FramedSignDisplayTarget
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import xfacthd.framedblocks.common.FBContent


@Mod(VSAdditionMod.MOD_ID)
class VSAdditionModForge {

    var CREATE_ACTIVE = false
    var CC_ACTIVE = false
    var FRAMEDBLOCKS_ACTIVE = false
    var COPYCATS_ACTIVE = false
    var CBC_ACTIVE = false
    var CBCMF_ACTIVE = false

    init {
        CREATE_ACTIVE = ModList.get().isLoaded("create")
        CC_ACTIVE = ModList.get().isLoaded("computercraft")
        FRAMEDBLOCKS_ACTIVE = ModList.get().isLoaded("framedblocks")
        COPYCATS_ACTIVE = ModList.get().isLoaded("copycats")
        CBC_ACTIVE = ModList.get().isLoaded("createbigcannnons")
        CBCMF_ACTIVE = ModList.get().isLoaded("cbcmodernwarfare")

        MOD_BUS.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }
        MOD_BUS.addListener { event: FMLCommonSetupEvent? ->
            serverSetup(
                event
            )
        }
        init()
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
                ), FBContent.BE_TYPE_FRAMED_SIGN.get()
            )

        if(CC_ACTIVE)
            Peripherals.register(ForgePeripheralProvider());
    }


    companion object {
        fun getModBus(): IEventBus = MOD_BUS
    }

}
