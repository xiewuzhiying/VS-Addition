package io.github.xiewuzhiying.vs_addition.fabric;

import dan200.computercraft.api.ComputerCraftAPI;
import io.github.xiewuzhiying.vs_addition.VSAdditionMod;
import io.github.xiewuzhiying.vs_addition.fabric.compats.computercraft.FabricPeripheralProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

import static io.github.xiewuzhiying.vs_addition.VSAdditionMod.init;

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
            ComputerCraftAPI.registerPeripheralProvider(new FabricPeripheralProvider());
    }
}
