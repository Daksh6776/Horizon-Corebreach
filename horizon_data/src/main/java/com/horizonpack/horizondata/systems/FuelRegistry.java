package com.horizonpack.horizondata.systems;

import com.horizonpack.horizondata.data.FuelDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom registry to track all available fuels across the Horizon mod ecosystem.
 */
public class FuelRegistry {
    // Internal storage mapped by the item's registry name (ResourceLocation)
    private static final Map<ResourceLocation, FuelDefinition> FUELS = new HashMap<>();

    static {
        // --- Pre-register common vanilla fuels ---
        register(new FuelDefinition(ResourceLocation.parse("minecraft:coal"), 1600, 0.5f, "coal"));
        register(new FuelDefinition(ResourceLocation.parse("minecraft:charcoal"), 1600, 0.5f, "coal"));
        // Using oak_log as the generic 'wood' representative; in production you may want a tag-based approach for all wood
        register(new FuelDefinition(ResourceLocation.parse("minecraft:oak_log"), 300, 0.2f, "wood"));

        // --- Pre-register custom modded fuels ---
        register(new FuelDefinition(ResourceLocation.fromNamespaceAndPath("horizondata", "oil_bucket"), 16000, 0.7f, "oil"));
        register(new FuelDefinition(ResourceLocation.fromNamespaceAndPath("horizondata", "diesel_bucket"), 24000, 0.85f, "oil"));
        register(new FuelDefinition(ResourceLocation.fromNamespaceAndPath("horizondata", "natural_gas_canister"), 12000, 0.9f, "gas"));

        // High-end nuclear fuels
        register(new FuelDefinition(ResourceLocation.fromNamespaceAndPath("horizondata", "uranium_fuel_rod"), 1000000, 1.0f, "nuclear"));
        register(new FuelDefinition(ResourceLocation.fromNamespaceAndPath("horizondata", "thorium_fuel_rod"), 800000, 0.95f, "nuclear"));
    }

    /**
     * Registers a new fuel definition.
     * @param fuel The FuelDefinition object to register.
     */
    public static void register(FuelDefinition fuel) {
        if (fuel != null && fuel.getItemId() != null) {
            FUELS.put(fuel.getItemId(), fuel);
        }
    }

    /**
     * Retrieves the FuelDefinition for a given item.
     * @param id The ResourceLocation of the item.
     * @return The FuelDefinition, or null if it is not a registered fuel.
     */
    public static FuelDefinition getFuel(ResourceLocation id) {
        return FUELS.get(id);
    }

    /**
     * Checks if a given item is registered as a fuel.
     * @param id The ResourceLocation of the item.
     * @return True if it is a fuel, false otherwise.
     */
    public static boolean isFuel(ResourceLocation id) {
        return FUELS.containsKey(id);
    }

    /**
     * Returns an unmodifiable map of all registered fuels.
     * @return A map of ResourceLocations to their FuelDefinitions.
     */
    public static Map<ResourceLocation, FuelDefinition> getAllFuels() {
        return Collections.unmodifiableMap(FUELS);
    }
}