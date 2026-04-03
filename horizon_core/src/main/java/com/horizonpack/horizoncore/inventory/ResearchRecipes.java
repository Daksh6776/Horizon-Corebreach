package com.horizonpack.horizoncore.inventory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import java.util.HashMap;
import java.util.Map;

public class ResearchRecipes {
    // Record to hold recipe data
    public record ResearchResult(Item item, int requiredIntel) {}

    private static final Map<Item, ResearchResult> RECIPES = new HashMap<>();

    static {
        // Input -> (Output Item, Required Intelligence Level)
        RECIPES.put(Items.STICK, new ResearchResult(HorizonRegistries.TECH_SCROLL.get(), 1));
        RECIPES.put(Items.BONE, new ResearchResult(HorizonRegistries.RESEARCH_NOTE.get(), 5));
        RECIPES.put(Items.BOOK, new ResearchResult(HorizonRegistries.SKILL_BOOK.get(), 10));
        RECIPES.put(Items.ECHO_SHARD, new ResearchResult(Items.NETHER_STAR, 50)); // High level example
    }

    public static ResearchResult getEntry(Item input) {
        return RECIPES.get(input);
    }
}