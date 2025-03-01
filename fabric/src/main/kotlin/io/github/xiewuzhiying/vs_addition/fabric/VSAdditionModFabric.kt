package io.github.xiewuzhiying.vs_addition.fabric

import dan200.computercraft.api.peripheral.PeripheralLookup
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.CC_ACTIVE
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.init
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.registerGenericPeripheralCommon
import io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralLookup.peripheralProvider
import io.github.xiewuzhiying.vs_addition.fabric.stuff.FakeRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.VSCoreFactory
import org.valkyrienskies.mod.fabric.common.FabricHooksImpl
import org.valkyrienskies.mod.fabric.common.VSFabricNetworking
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric

object VSAdditionModFabric {
    fun onInitialize() {
        ValkyrienSkiesModFabric().onInitialize();
        val isClient = FabricLoader.getInstance().environmentType == EnvType.CLIENT
        val networking = VSFabricNetworking(isClient)
        val hooks = FabricHooksImpl(networking)
        val vsCore = if (isClient) {
            VSCoreFactory.instance.newVsCoreClient(hooks)
        } else {
            VSCoreFactory.instance.newVsCoreServer(hooks)
        }

        networking.register(vsCore.hooks)
        init(vsCore)
        if (CC_ACTIVE) {
            registerGenericPeripheralCommon()
            PeripheralLookup.get().registerFallback { level: Level, blockPos: BlockPos, _: BlockState, _: BlockEntity?, _: Direction -> peripheralProvider(level, blockPos) }
        }
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            PlatformUtilsImpl.minecraft = server
        }
    }

    fun onInitializeClient() {
        initClient()
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(FakeRenderer())
        WorldRenderEvents.LAST.register(FakeRenderer())
    }
}