package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class HorizonRecipeProvider extends RecipeProvider {

    public HorizonRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        // Research Bench Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HorizonRegistries.RESEARCH_BENCH.get())
                .pattern("SSS") // S = Smooth Stone Slab
                .pattern("C C") // C = Cobblestone
                .pattern("C C")
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('C', Blocks.COBBLESTONE)
                // unlockedBy is required for the Minecraft recipe book
                .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
                .save(output);

        // You can add ShapelessRecipeBuilder or SimpleCookingRecipeBuilder here later!
    }
}