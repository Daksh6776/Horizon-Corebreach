package com.horizonpack.horizondata.core;

import com.horizonpack.horizondata.blocks.ores.OreBlockRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(HorizonData.MODID)
public class HorizonData {
    public static final String MODID = "horizondata";

    // ONLY ONE CONSTRUCTOR ALLOWED!
    public HorizonData(IEventBus modEventBus) {
        // 1. Queue up all recipes and register everything to the event bus
        DataRegistries.register(modEventBus);

        // 2. Wake up the automated Ore Registry
        OreBlockRegistry.init();

        // 3. Register the setup method
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Initialization code (like network packets) goes here later
    }
}