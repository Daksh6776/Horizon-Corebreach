package com.horizonpack.horizoncore.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;

public class TechnologyProvider implements DataProvider {

    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public TechnologyProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.packOutput = packOutput;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        // 1. Generate Flint Knapping
        futures.add(generateTech(cache, "flint_knapping",
                "stone_age",
                new String[]{}, // No prerequisites
                2400,
                "minecraft:gravel", 4,
                "unlock_recipe_category", "horizoncore:flint_tools",
                "minecraft:textures/item/flint.png",
                "tech.horizoncore.flint_knapping.desc"
        ));

        // 2. Generate Fire Making
        futures.add(generateTech(cache, "fire_making",
                "stone_age",
                new String[]{"horizoncore:flint_knapping"}, // Requires Flint Knapping
                6000,
                "minecraft:flint", 2, // Cost (simplified for the builder)
                "unlock_block", "minecraft:campfire",
                "minecraft:textures/item/campfire.png",
                "tech.horizoncore.fire_making.desc"
        ));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    // A helper method to construct the JSON object and save it to the correct folder
    private CompletableFuture<?> generateTech(CachedOutput cache, String name, String age, String[] prereqs, int time, String costItem, int costCount, String effectType, String effectTarget, String icon, String desc) {

        JsonObject json = new JsonObject();
        json.addProperty("required_age", age);

        JsonArray prereqArray = new JsonArray();
        for (String p : prereqs) prereqArray.add(p);
        json.add("prerequisites", prereqArray);

        json.addProperty("research_time_ticks", time);

        JsonArray costArray = new JsonArray();
        JsonObject costObj = new JsonObject();
        costObj.addProperty("item", costItem);
        costObj.addProperty("count", costCount);
        costArray.add(costObj);
        json.add("research_cost", costArray);

        JsonArray effectArray = new JsonArray();
        JsonObject effectObj = new JsonObject();
        effectObj.addProperty("type", effectType);
        if (effectType.equals("unlock_recipe_category")) effectObj.addProperty("category", effectTarget);
        if (effectType.equals("unlock_block")) effectObj.addProperty("block", effectTarget);
        effectArray.add(effectObj);
        json.add("effects", effectArray);

        json.addProperty("icon_path", icon);
        json.addProperty("description_key", desc);

        // Define the output path: src/generated/resources/data/horizoncore/horizon_technologies/[name].json
        Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(HorizonCore.MODID)
                .resolve("horizon_technologies")
                .resolve(name + ".json");

        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "HorizonCore Technologies";
    }
}