package com.horizonpack.horizoncore.Horizoncore;

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
    public static final String VERSION = "1.0.0";

    public HorizonCore(IEventBus modEventBus, ModContainer modContainer) {
        // Register configs
        modContainer.registerConfig(ModConfig.Type.COMMON, HorizonConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, HorizonConfig.CLIENT_SPEC);

        // Register all deferred registers onto the mod event bus
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

        // Lifecycle listeners on the mod bus
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(HorizonCapabilities::register);
        modEventBus.addListener(HorizonPacketHandler::register);

        // Game event listeners on the NeoForge bus
        NeoForge.EVENT_BUS.register(HorizonEventHandlers.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HorizonRegistries.init();
            TechnologyGraph.buildGraph(); // Build the DAG after all techs are registered
        });
    }
}