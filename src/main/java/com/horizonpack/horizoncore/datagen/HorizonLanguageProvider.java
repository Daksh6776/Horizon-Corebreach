package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class HorizonLanguageProvider extends LanguageProvider {
    public HorizonLanguageProvider(PackOutput output) {
        super(output, HorizonCore.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Blocks
        add(HorizonRegistries.RESEARCH_BENCH.get(), "Research Bench");
        add(HorizonRegistries.LIBRARY.get(), "Library");
        add(HorizonRegistries.UNIVERSITY.get(), "University");
        add(HorizonRegistries.LABORATORY.get(), "Laboratory");

        // Items
        add(HorizonRegistries.TECH_SCROLL.get(), "Technology Scroll");
        add(HorizonRegistries.RESEARCH_NOTE.get(), "Research Note");
        add(HorizonRegistries.SKILL_BOOK.get(), "Skill Book");

        // Creative Tab
        add("itemGroup.horizon_core_tab", "Horizon Core");

        // Inside addTranslations()
        add("block.horizoncore.research_bench", "Research Bench");
        add("block.horizoncore.library", "Ancient Library");
        add("block.horizoncore.university", "Settlement University");
        add("block.horizoncore.laboratory", "Scientific Laboratory");
    }

}