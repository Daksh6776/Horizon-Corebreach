package com.horizonpack.horizondata.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;

public class CarpentryRecipe implements Recipe<CraftingInput> {
    @Override
    public boolean matches(CraftingInput inv, net.minecraft.world.level.Level level) { return false; }
    @Override
    public ItemStack assemble(CraftingInput inv, net.minecraft.core.HolderLookup.Provider access) { return ItemStack.EMPTY; }
    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider access) { return ItemStack.EMPTY; }
    @Override
    public RecipeSerializer<?> getSerializer() { return null; }
    @Override
    public RecipeType<?> getType() { return null; }
}
