package io.github.xiewuzhiying.vs_addition.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import io.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralLookup;
import net.fabricmc.api.ModInitializer;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

import static io.github.xiewuzhiying.vs_addition.VSAdditionMod.init;

public class VSAdditionModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // force VS2 to load before eureka
        new ValkyrienSkiesModFabric().onInitialize();

        init();

        if(VSAdditionMod.getCC_ACTIVE())
            PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) -> FabricPeripheralLookup.INSTANCE.peripheralProvider(level, blockPos));
    }
}
