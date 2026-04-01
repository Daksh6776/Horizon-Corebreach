package com.horizonpack.horizoncore.core;

import com.horizonpack.horizoncore.blocks.*;
import com.horizonpack.horizoncore.blockentities.*;
import com.horizonpack.horizoncore.items.*;
import com.horizonpack.horizoncore.data.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class HorizonRegistries {

    // ── Custom registry keys ──────────────────────────────────────────────────
    public static final ResourceKey<Registry<TechnologyData>> TECHNOLOGY_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "technologies"));
    public static final ResourceKey<Registry<SkillDefinition>> SKILL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "skills"));
    public static final ResourceKey<Registry<Profession>> PROFESSION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "professions"));
    public static final ResourceKey<Registry<NutrientType>> NUTRIENT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "nutrients"));
    public static final ResourceKey<Registry<DiseaseType>> DISEASE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "diseases"));
    public static final ResourceKey<Registry<WeatherType>> WEATHER_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "weather_types"));
    public static final ResourceKey<Registry<DisasterType>> DISASTER_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "disasters"));

    // ── Deferred registers for custom types ──────────────────────────────────
    public static final DeferredRegister<TechnologyData> TECHNOLOGIES = DeferredRegister.create(TECHNOLOGY_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<SkillDefinition> SKILLS = DeferredRegister.create(SKILL_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<Profession> PROFESSIONS = DeferredRegister.create(PROFESSION_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<NutrientType> NUTRIENTS = DeferredRegister.create(NUTRIENT_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<DiseaseType> DISEASES = DeferredRegister.create(DISEASE_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<WeatherType> WEATHER_TYPES = DeferredRegister.create(WEATHER_REGISTRY_KEY, HorizonCore.MODID);
    public static final DeferredRegister<DisasterType> DISASTERS = DeferredRegister.create(DISASTER_REGISTRY_KEY, HorizonCore.MODID);

    // ── Vanilla-backed deferred registers (1.21.1 Syntax) ────────────────────
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HorizonCore.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HorizonCore.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HorizonCore.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, HorizonCore.MODID);

    // ── Block registrations ───────────────────────────────────────────────────
    public static final DeferredBlock<ResearchBenchBlock> RESEARCH_BENCH = BLOCKS.register("research_bench",
            () -> new ResearchBenchBlock(Block.Properties.of().strength(2.5f, 6.0f).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<LibraryBlock> LIBRARY = BLOCKS.register("library",
            () -> new LibraryBlock(Block.Properties.of().strength(2.0f, 5.0f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<UniversityBlock> UNIVERSITY = BLOCKS.register("university",
            () -> new UniversityBlock(Block.Properties.of().strength(3.0f, 8.0f).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<LaboratoryBlock> LABORATORY = BLOCKS.register("laboratory",
            () -> new LaboratoryBlock(Block.Properties.of().strength(2.5f, 6.0f).requiresCorrectToolForDrops().noOcclusion()));

    // ── Item registrations ────────────────────────────────────────────────────
    public static final DeferredItem<BlockItem> RESEARCH_BENCH_ITEM = ITEMS.registerSimpleBlockItem("research_bench", RESEARCH_BENCH);
    public static final DeferredItem<BlockItem> LIBRARY_ITEM = ITEMS.registerSimpleBlockItem("library", LIBRARY);
    public static final DeferredItem<BlockItem> UNIVERSITY_ITEM = ITEMS.registerSimpleBlockItem("university", UNIVERSITY);
    public static final DeferredItem<BlockItem> LABORATORY_ITEM = ITEMS.registerSimpleBlockItem("laboratory", LABORATORY);

    public static final DeferredItem<TechScrollItem> TECH_SCROLL = ITEMS.register("tech_scroll", () -> new TechScrollItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<ResearchNoteItem> RESEARCH_NOTE = ITEMS.register("research_note", () -> new ResearchNoteItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<SkillBookItem> SKILL_BOOK = ITEMS.register("skill_book", () -> new SkillBookItem(new Item.Properties().stacksTo(1)));

    // ── BlockEntity registrations ─────────────────────────────────────────────
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResearchBenchBlockEntity>> RESEARCH_BENCH_BE =
            BLOCK_ENTITIES.register("research_bench", () -> BlockEntityType.Builder.of(ResearchBenchBlockEntity::new, RESEARCH_BENCH.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LibraryBlockEntity>> LIBRARY_BE =
            BLOCK_ENTITIES.register("library", () -> BlockEntityType.Builder.of(LibraryBlockEntity::new, LIBRARY.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UniversityBlockEntity>> UNIVERSITY_BE =
            BLOCK_ENTITIES.register("university", () -> BlockEntityType.Builder.of(UniversityBlockEntity::new, UNIVERSITY.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LaboratoryBlockEntity>> LABORATORY_BE =
            BLOCK_ENTITIES.register("laboratory", () -> BlockEntityType.Builder.of(LaboratoryBlockEntity::new, LABORATORY.get()).build(null));

    // ── Menu Registrations ───────────────────────────────────────────────────

    public static final DeferredHolder<MenuType<?>, MenuType<com.horizonpack.horizoncore.inventory.ResearchBenchMenu>> RESEARCH_BENCH_MENU =
            MENU_TYPES.register("research_bench", () ->
                    new MenuType<>(com.horizonpack.horizoncore.inventory.ResearchBenchMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<?>> LIBRARY_MENU =
            MENU_TYPES.register("library", () -> new MenuType<>(ChestMenu::sixRows, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));


    public static final DeferredHolder<MenuType<?>, MenuType<?>> UNIVERSITY_MENU =
            MENU_TYPES.register("university", () -> new MenuType<>(ChestMenu::threeRows, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));


    public static final DeferredHolder<MenuType<?>, MenuType<?>> LABORATORY_MENU =
            MENU_TYPES.register("laboratory", () -> new MenuType<>(ChestMenu::threeRows, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));





    // ── Creative Tab Registration ─────────────────────────────────────────────
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HorizonCore.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HORIZON_TAB =
            CREATIVE_MODE_TABS.register("horizon_core_tab", () -> CreativeModeTab.builder()
                    .title(Component.literal("Horizon Core")) // The name of the tab in-game
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> RESEARCH_BENCH_ITEM.get().getDefaultInstance()) // The icon for the tab
                    .displayItems((parameters, output) -> {
                        // Add all your items here so they show up in the menu!
                        output.accept(RESEARCH_BENCH_ITEM.get());
                        output.accept(LIBRARY_ITEM.get());
                        output.accept(UNIVERSITY_ITEM.get());
                        output.accept(LABORATORY_ITEM.get());
                        output.accept(TECH_SCROLL.get());
                        output.accept(RESEARCH_NOTE.get());
                        output.accept(SKILL_BOOK.get());
                    }).build());

    public static void init() {}

}