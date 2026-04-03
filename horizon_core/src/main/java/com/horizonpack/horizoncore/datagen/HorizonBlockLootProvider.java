package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;

public class HorizonBlockLootProvider extends BlockLootSubProvider {

    protected HorizonBlockLootProvider(HolderLookup.Provider provider) {
        // We pass empty sets and default flags for standard block behavior
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        // dropSelf() means "When I break this block, drop the item version of this block"
        this.dropSelf(HorizonRegistries.RESEARCH_BENCH.get());

        // If you had custom ores, you would do something like:
        // this.add(HorizonRegistries.CUSTOM_ORE.get(), block -> createOreDrop(block, HorizonRegistries.RAW_CUSTOM_ORE.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // This tells the DataGenerator to strictly check that all our custom blocks have a loot table mapped.
        // If you forget one, the DataGen task will throw a helpful error!
        return List.of(
                HorizonRegistries.RESEARCH_BENCH.get()
                // Add your other blocks here separated by commas
        );
    }
}