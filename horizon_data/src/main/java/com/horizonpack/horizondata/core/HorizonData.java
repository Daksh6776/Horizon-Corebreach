package com.horizonpack.horizondata.core;

import com.horizonpack.horizondata.blocks.HorizonDataBlocks;
import com.horizonpack.horizondata.blocks.ores.OreBlockRegistry;
import com.horizonpack.horizondata.items.HorizonDataItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(HorizonData.MODID)
public class HorizonData {
    public static final String MODID = "horizondata";

    public HorizonData(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DataConfig.COMMON_SPEC);

        // --- ADD THIS LINE ---
        // Force the ore blocks to populate the DeferredRegister
        OreBlockRegistry.init();

        // Register all deferred registers
        DataRegistries.BLOCKS.register(modEventBus);
        DataRegistries.ITEMS.register(modEventBus);

        // Force the registry classes to load
        var blocks = HorizonDataBlocks.LOOSE_PEBBLES;
        var items = HorizonDataItems.RAW_TIN;
    }
}