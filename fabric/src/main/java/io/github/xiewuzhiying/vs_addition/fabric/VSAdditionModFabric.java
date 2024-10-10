package io.github.xiewuzhiying.vs_addition.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import io.github.xiewuzhiying.vs_addition.stuff.EntityFreshCaller;
import io.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralLookup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

import static io.github.xiewuzhiying.vs_addition.VSAdditionMod.init;
import static io.github.xiewuzhiying.vs_addition.compats.computercraft.PeripheralCommon.registerGenericPeripheralCommon;

public class VSAdditionModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // force VS2 to load before eureka
        new ValkyrienSkiesModFabric().onInitialize();

        init();

        if(VSAdditionMod.getCC_ACTIVE()) {
            registerGenericPeripheralCommon();
            PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) -> FabricPeripheralLookup.INSTANCE.peripheralProvider(level, blockPos));
        }

        ServerEntityEvents.ENTITY_LOAD.register((EntityFreshCaller.INSTANCE::freshEntityInShipyard));
    }
}
