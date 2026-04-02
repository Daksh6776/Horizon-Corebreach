package com.horizonpack.horizondata.datagen;

import com.horizonpack.horizondata.blocks.ores.OreBlockRegistry;
import com.horizonpack.horizondata.data.OreDefinition;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class HorizonDataWorldGen {

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (OreDefinition ore : OreBlockRegistry.ALL_ORES) {
            String baseName = ore.name();

            // Replicate the naming logic from OreBlockRegistry
            String stoneId = (baseName.contains("deposit") || baseName.contains("pocket")) ? baseName : baseName + "_ore";
            String deepslateId = "deepslate_" + stoneId;

            // Dynamically look up the blocks from the registry using the IDs we generated
            var stoneBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("horizondata", stoneId));
            var deepslateBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("horizondata", deepslateId));

            // Define replacement targets (Stone and Deepslate)
            List<OreConfiguration.TargetBlockState> targets = List.of(
                    OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                            stoneBlock.defaultBlockState()),
                    OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                            deepslateBlock.defaultBlockState())
            );

            // Register Configured Feature using ore.name() and ore.veinSize()
            context.register(ResourceKey.create(Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath("horizondata", baseName + "_ore")),
                    new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, ore.veinSize())));
        }
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        var lookup = context.lookup(Registries.CONFIGURED_FEATURE);

        for (OreDefinition ore : OreBlockRegistry.ALL_ORES) {
            String baseName = ore.name();

            // Get the configured feature we registered above
            var configured = lookup.getOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath("horizondata", baseName + "_ore")));

            // Register Placed Feature using new method names: .count(), .minY(), and .maxY()
            context.register(ResourceKey.create(Registries.PLACED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath("horizondata", baseName + "_ore")),
                    new PlacedFeature(configured, List.of(
                            CountPlacement.of(ore.count()),
                            InSquarePlacement.spread(),
                            HeightRangePlacement.triangle(
                                    VerticalAnchor.absolute(ore.minY()),
                                    VerticalAnchor.absolute(ore.maxY())
                            ),
                            BiomeFilter.biome()
                    ))
            );
        }
    }
}