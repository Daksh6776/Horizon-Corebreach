package com.horizonpack.horizondata.datagen;

import com.horizonpack.horizondata.core.HorizonData;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = HorizonData.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGeneratorHandler {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // WorldGen Provider
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                output, lookupProvider, new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, HorizonDataWorldGen::bootstrapConfigured)
                .add(Registries.PLACED_FEATURE, HorizonDataWorldGen::bootstrapPlaced),
                Set.of(HorizonData.MODID)));

        // You will add BlockStateProvider and RecipeProvider here in Turn 3!
    }
}