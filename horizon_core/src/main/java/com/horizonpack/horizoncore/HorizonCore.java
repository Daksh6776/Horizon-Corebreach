package com.horizonpack.horizoncore;

import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.core.HorizonConfig;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.data.TechnologyGraph;
import com.horizonpack.horizoncore.events.HorizonEventHandlers;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(HorizonCore.MODID)
public class HorizonCore {
    public static final String MODID = "horizoncore";

    public HorizonCore(IEventBus modEventBus, ModContainer modContainer) {
        // Register the configs so the simulationRadius and other settings appear in /config
        modContainer.registerConfig(ModConfig.Type.COMMON, HorizonConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, HorizonConfig.CLIENT_SPEC);

        // All registries must be registered to the mod bus
        HorizonRegistries.TECHNOLOGIES.register(modEventBus);
        HorizonRegistries.SKILLS.register(modEventBus);
        HorizonRegistries.PROFESSIONS.register(modEventBus);
        HorizonRegistries.NUTRIENTS.register(modEventBus);
        HorizonRegistries.DISEASES.register(modEventBus);
        HorizonRegistries.WEATHER_TYPES.register(modEventBus);
        HorizonRegistries.DISASTERS.register(modEventBus);
        HorizonRegistries.BLOCKS.register(modEventBus);
        HorizonRegistries.BLOCK_ENTITIES.register(modEventBus);
        HorizonRegistries.ITEMS.register(modEventBus);
        HorizonRegistries.MENU_TYPES.register(modEventBus);
        HorizonRegistries.CREATIVE_MODE_TABS.register(modEventBus);

        // Register the Age Condition registry for the recipe gating system
        HorizonRegistries.CONDITION_CODECS.register(modEventBus);

        // Mod bus listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(HorizonCapabilities::register);
        modEventBus.addListener(HorizonPacketHandler::register);

        // Forge bus listeners
        NeoForge.EVENT_BUS.register(HorizonEventHandlers.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HorizonRegistries.init();
            // Build the technology hierarchy once everything is registered
            TechnologyGraph.buildGraph();
        });
    }
}