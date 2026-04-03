package com.horizonpack.horizoncore.registry;

import com.horizonpack.horizoncore.HorizonCore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
// Import for DeferredItem available in NeoForge 1.21+
// import net.neoforged.neoforge.registries.DeferredItem;

public class CoreItems {
    // Create the DeferredRegister using the NeoForge specific createItems method
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HorizonCore.MODID);

    // Example item registration:
    // public static final DeferredItem<Item> CORE_GEM = ITEMS.register("core_gem",
    //         () -> new Item(new Item.Properties()));

    // Method to be called in the HorizonCore constructor
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}