package com.horizonpack.horizoncore.datagen;

import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = HorizonCore.MODID, bus = EventBusSubscriber.Bus.MOD)
public class HorizonDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 1. Custom Technology Provider (from last turn)
        generator.addProvider(event.includeServer(), new TechnologyProvider(packOutput, lookupProvider));

        // 2. Standard Recipe Provider
        generator.addProvider(event.includeServer(), new HorizonRecipeProvider(packOutput, lookupProvider));

        // 3. Standard Loot Table Provider (Block Drops)
        generator.addProvider(event.includeServer(), new LootTableProvider(
                packOutput,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(HorizonBlockLootProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));
    }
}