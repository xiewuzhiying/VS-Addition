package io.github.xiewuzhiying.vs_addition.forge

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours
import dev.architectury.platform.forge.EventBuses
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig
import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.init
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.registerGenericPeripheralCommon
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.ForgePeripheralProvider
import io.github.xiewuzhiying.vs_addition.forge.compats.computercraft.PeripheralForge.registerGenericPeripheralForge
import io.github.xiewuzhiying.vs_addition.forge.compats.create.content.redstone.display_link.target.FramedSignDisplayTarget
import io.github.xiewuzhiying.vs_addition.context.airpocket.FakeAirPocketClient
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES
import net.minecraftforge.eventbus.api.IEventBus
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
object VSAdditionModForge {
    init {
        EventBuses.registerModEventBus(VSAdditionMod.MOD_ID, MOD_CONTEXT.getKEventBus())
        init()

        getModBus().addListener(this::clientSetup)

        getModBus().addListener(this::commonSetup)

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
            ConfigScreenHandler.ConfigScreenFactory { _, parent ->
                VSClothConfig.createConfigScreenFor(
                    parent,
                    VSConfigClass.getRegisteredConfig(VSAdditionConfig::class.java)
                )
            }
        }
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        initClient()
        getForgeBus().addListener(this::renderWorld)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        if (VSAdditionMod.FRAMEDBLOCKS_ACTIVE && VSAdditionMod.CREATE_ACTIVE)
            AllDisplayBehaviours.assignBlockEntity(
                AllDisplayBehaviours.register(
                    ResourceLocation(
                        VSAdditionMod.MOD_ID,
                        "framed_sign_display_target"
                    ),
                    FramedSignDisplayTarget()
                ), FBContent.BE_TYPE_FRAMED_SIGN.get()
            )

        if (VSAdditionMod.CC_ACTIVE) {
            registerGenericPeripheralCommon()
            registerGenericPeripheralForge()
            VSAdditionModForgePeripheralLookup.registerFallback()
        }
    }

    private fun renderWorld(event: RenderLevelStageEvent) {
        if (!VSAdditionConfig.COMMON.experimental.fakeAirPocket) return
        if (VSAdditionConfig.CLIENT.experimental.cullWaterSurfaceInFakeAirPocket && event.stage == AFTER_BLOCK_ENTITIES) {
            val mc = Minecraft.getInstance()
            val bufferSource = mc.renderBuffers().bufferSource();
            FakeAirPocketClient.render(event.poseStack, event.camera, bufferSource)
            bufferSource.endBatch()
        } else if (VSAdditionConfig.CLIENT.experimental.highLightFakedAirPocket && event.stage == AFTER_PARTICLES) {
            val mc = Minecraft.getInstance()
            val bufferSource = mc.renderBuffers().bufferSource();
            FakeAirPocketClient.renderHighLight(event.poseStack, event.camera, bufferSource)
            bufferSource.endBatch()
        }
    }

    @JvmStatic
    fun getModBus(): IEventBus = MOD_BUS
    @JvmStatic
    fun getForgeBus(): IEventBus = FORGE_BUS
}
