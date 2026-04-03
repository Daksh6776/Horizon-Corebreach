package com.horizonpack.horizoncore.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

        // ==========================================
        // STONE AGE TECHNOLOGIES
        // ==========================================
        futures.add(new TechBuilder("flint_knapping", "stone_age")
                .time(2400)
                .cost("minecraft:gravel", 4)
                .effect("unlock_recipe_category", "category", "horizoncraft:flint_tools")
                .desc("horizoncore.tech.flint_knapping.desc")
                .icon("horizoncore:textures/tech/flint_knapping.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("fire_making", "stone_age")
                .prereq("horizoncore:flint_knapping")
                .time(6000)
                .cost("minecraft:flint", 4)
                .cost("minecraft:stick", 2)
                .effect("unlock_recipe_category", "category", "horizoncraft:fire_tools")
                .effect("unlock_block", "block", "horizoncraft:campfire_primitive")
                .desc("horizoncore.tech.fire_making.desc")
                .icon("horizoncore:textures/tech/fire_making.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("basket_weaving", "stone_age")
                .time(3000)
                .cost("minecraft:short_grass", 16)
                .effect("unlock_recipe_category", "category", "horizoncraft:baskets")
                .desc("horizoncore.tech.basket_weaving.desc")
                .icon("horizoncore:textures/tech/basket_weaving.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("primitive_pottery", "stone_age")
                .prereq("horizoncore:fire_making")
                .time(8000)
                .cost("minecraft:clay_ball", 16)
                .effect("unlock_block", "block", "horizoncraft:primitive_kiln")
                .desc("horizoncore.tech.primitive_pottery.desc")
                .icon("horizoncore:textures/tech/primitive_pottery.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("cave_painting", "stone_age")
                .prereq("horizoncore:fire_making")
                .time(4000)
                .cost("minecraft:charcoal", 4)
                .cost("minecraft:leather", 1)
                .effectSkillCap("HISTORY", 10)
                .desc("horizoncore.tech.cave_painting.desc")
                .icon("horizoncore:textures/tech/cave_painting.png")
                .save(cache, packOutput));

        // ==========================================
        // COPPER AGE TECHNOLOGIES
        // ==========================================
        futures.add(new TechBuilder("copper_smelting", "copper_age")
                .prereq("horizoncore:primitive_pottery")
                .time(12000)
                .cost("minecraft:raw_copper", 16)
                .cost("minecraft:charcoal", 8)
                .effect("unlock_recipe_category", "category", "horizoncraft:copper_tools")
                .effectAdvanceAge("copper_age")
                .desc("horizoncore.tech.copper_smelting.desc")
                .icon("horizoncore:textures/tech/copper_smelting.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("basic_agriculture", "copper_age")
                .prereq("horizoncore:basket_weaving")
                .time(10000)
                .cost("minecraft:wheat_seeds", 16)
                .effectSkillCap("FARMING", 25)
                .desc("horizoncore.tech.basic_agriculture.desc")
                .icon("horizoncore:textures/tech/basic_agriculture.png")
                .save(cache, packOutput));

        // ==========================================
        // BRONZE AGE TECHNOLOGIES
        // ==========================================
        futures.add(new TechBuilder("bronze_alloy", "bronze_age")
                .prereq("horizoncore:copper_smelting")
                .time(24000)
                .cost("minecraft:copper_ingot", 12)
                .cost("minecraft:raw_gold", 4) // Assuming tin/zinc isn't in vanilla, using gold as a placeholder
                .effect("unlock_recipe_category", "category", "horizoncraft:bronze_working")
                .effectAdvanceAge("bronze_age")
                .desc("horizoncore.tech.bronze_alloy.desc")
                .icon("horizoncore:textures/tech/bronze_alloy.png")
                .save(cache, packOutput));

        futures.add(new TechBuilder("writing", "bronze_age")
                .time(30000)
                .cost("minecraft:paper", 10)
                .cost("minecraft:ink_sac", 5)
                .effectSkillCap("LITERACY", 50)
                .desc("horizoncore.tech.writing.desc")
                .icon("horizoncore:textures/tech/writing.png")
                .save(cache, packOutput));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "HorizonCore Technologies";
    }

    /**
     * A highly flexible builder to generate Tech JSONs cleanly.
     */
    private static class TechBuilder {
        private final JsonObject json = new JsonObject();
        private final JsonArray prereqs = new JsonArray();
        private final JsonArray costs = new JsonArray();
        private final JsonArray effects = new JsonArray();
        private final String name;
        private final String age;

        public TechBuilder(String name, String age) {
            this.name = name;
            this.age = age;
            json.addProperty("required_age", age);
        }

        public TechBuilder prereq(String prereq) {
            prereqs.add(prereq);
            return this;
        }

        public TechBuilder time(int ticks) {
            json.addProperty("research_time_ticks", ticks);
            return this;
        }

        public TechBuilder cost(String item, int count) {
            JsonObject c = new JsonObject();
            c.addProperty("item", item);
            c.addProperty("count", count);
            costs.add(c);
            return this;
        }

        public TechBuilder effect(String type, String key, String val) {
            JsonObject e = new JsonObject();
            e.addProperty("type", type);
            e.addProperty(key, val);
            effects.add(e);
            return this;
        }

        public TechBuilder effectAdvanceAge(String newAge) {
            JsonObject e = new JsonObject();
            e.addProperty("type", "advance_age");
            e.addProperty("newAge", newAge);
            effects.add(e);
            return this;
        }

        public TechBuilder effectSkillCap(String skill, int maxLevel) {
            JsonObject e = new JsonObject();
            e.addProperty("type", "unlock_skill_cap");
            e.addProperty("skill", skill);
            e.addProperty("new_max_level", maxLevel);
            effects.add(e);
            return this;
        }

        public TechBuilder desc(String desc) {
            json.addProperty("description", desc);
            return this;
        }

        public TechBuilder icon(String icon) {
            json.addProperty("icon", icon);
            return this;
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput output) {
            json.add("prerequisites", prereqs);
            json.add("research_cost", costs);
            json.add("effects", effects);

            // Saves perfectly into data/horizoncore/horizon_technologies/<age>/<name>.json
            Path path = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(HorizonCore.MODID)
                    .resolve("horizon_technologies")
                    .resolve(age)
                    .resolve(name + ".json");

            return DataProvider.saveStable(cache, json, path);
        }
    }
}