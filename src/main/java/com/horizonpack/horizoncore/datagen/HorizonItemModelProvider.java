package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class HorizonItemModelProvider extends ItemModelProvider {
    public HorizonItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HorizonCore.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // basicItem assumes the texture is located at textures/item/name.png
        basicItem(HorizonRegistries.TECH_SCROLL.get());
        basicItem(HorizonRegistries.RESEARCH_NOTE.get());
        basicItem(HorizonRegistries.SKILL_BOOK.get());
    }
}