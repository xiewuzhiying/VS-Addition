package io.github.xiewuzhiying.vs_addition.fabric

import io.github.xiewuzhiying.vs_addition.VSAdditionMod.CC_ACTIVE
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.init
import io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient
import io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.registerGenericPeripheralCommon
import io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralLookup.peripheralProvider
import io.github.xiewuzhiying.vs_addition.fabric.stuff.FakeRenderer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric

object VSAdditionModFabric {
    fun onInitialize() {
        ValkyrienSkiesModFabric().onInitialize();
        init()
        if (CC_ACTIVE) {
            registerGenericPeripheralCommon()
            VSAdditionModFabricPeripheralLookup.registerFallback()
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