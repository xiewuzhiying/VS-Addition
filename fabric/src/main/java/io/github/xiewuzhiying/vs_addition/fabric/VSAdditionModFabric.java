package io.github.xiewuzhiying.vs_addition.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

import static com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour.peripheralProvider;
import static io.github.xiewuzhiying.vs_addition.VSAdditionMod.init;
import static io.github.xiewuzhiying.vs_addition.VSAdditionMod.initClient;

public class VSAdditionModFabric implements ModInitializer {
    private static  boolean VS2_ACTIVE = false;

    @Override
    public void onInitialize() {
        // force VS2 to load before eureka
        VS2_ACTIVE = FabricLoader.getInstance().isModLoaded("valkyrienskies");

        if(VS2_ACTIVE)
            new ValkyrienSkiesModFabric().onInitialize();

        init();

        if(VSAdditionMod.getCC_ACTIVE())
            PeripheralLookup.get().registerFallback((level, blockPos, blockState, blockEntity, direction) -> peripheralProvider(level, blockPos));
    }
}
