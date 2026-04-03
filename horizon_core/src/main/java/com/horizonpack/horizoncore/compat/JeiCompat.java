package com.horizonpack.horizoncore.compat;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JeiCompat implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        // Here you will register your custom Research Bench category once built.
        // registration.addRecipeCategories(new ResearchBenchCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Binds the Research Bench block to your custom recipe category
        // registration.addRecipeCatalyst(new ItemStack(HorizonRegistries.RESEARCH_BENCH_ITEM.get()), YOUR_CUSTOM_RECIPE_TYPE);
    }
}