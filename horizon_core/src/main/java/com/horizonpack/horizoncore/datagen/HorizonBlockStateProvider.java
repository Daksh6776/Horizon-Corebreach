package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class HorizonBlockStateProvider extends BlockStateProvider {
    public HorizonBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HorizonCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // simpleBlockWithItem creates the block model AND the inventory item model!
        simpleBlockWithItem(HorizonRegistries.RESEARCH_BENCH.get(), cubeAll(HorizonRegistries.RESEARCH_BENCH.get()));
        simpleBlockWithItem(HorizonRegistries.LIBRARY.get(), cubeAll(HorizonRegistries.LIBRARY.get()));
        simpleBlockWithItem(HorizonRegistries.UNIVERSITY.get(), cubeAll(HorizonRegistries.UNIVERSITY.get()));
        simpleBlockWithItem(HorizonRegistries.LABORATORY.get(), cubeAll(HorizonRegistries.LABORATORY.get()));
    }
}