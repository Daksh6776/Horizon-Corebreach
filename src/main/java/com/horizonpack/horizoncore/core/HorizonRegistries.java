package com.horizonpack.horizoncore.core;

import com.horizonpack.horizoncore.blocks.*;
import com.horizonpack.horizoncore.blockentities.*;
import com.horizonpack.horizoncore.items.*;
import com.horizonpack.horizoncore.data.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

public class HorizonRegistries {

    public static final ResourceKey<Registry<TechnologyData>> TECHNOLOGY_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "technologies"));

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.create(Registries.BLOCK, HorizonCore.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.create(Registries.ITEM, HorizonCore.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HorizonCore.MODID);

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
    public static final DeferredItem<BlockItem> RESEARCH_BENCH_ITEM = ITEMS.registerBlockItem("research_bench", RESEARCH_BENCH);
    public static final DeferredItem<BlockItem> LIBRARY_ITEM = ITEMS.registerBlockItem("library", LIBRARY);
    public static final DeferredItem<BlockItem> UNIVERSITY_ITEM = ITEMS.registerBlockItem("university", UNIVERSITY);
    public static final DeferredItem<BlockItem> LABORATORY_ITEM = ITEMS.registerBlockItem("laboratory", LABORATORY);

    public static final DeferredItem<TechScrollItem> TECH_SCROLL = ITEMS.register("tech_scroll",
            () -> new TechScrollItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<ResearchNoteItem> RESEARCH_NOTE = ITEMS.register("research_note",
            () -> new ResearchNoteItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SkillBookItem> SKILL_BOOK = ITEMS.register("skill_book",
            () -> new SkillBookItem(new Item.Properties().stacksTo(1)));

    // ── BlockEntity registrations ─────────────────────────────────────────────
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResearchBenchBlockEntity>> RESEARCH_BENCH_BE =
            BLOCK_ENTITIES.register("research_bench", () -> BlockEntityType.Builder.of(ResearchBenchBlockEntity::new, RESEARCH_BENCH.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LibraryBlockEntity>> LIBRARY_BE =
            BLOCK_ENTITIES.register("library", () -> BlockEntityType.Builder.of(LibraryBlockEntity::new, LIBRARY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UniversityBlockEntity>> UNIVERSITY_BE =
            BLOCK_ENTITIES.register("university", () -> BlockEntityType.Builder.of(UniversityBlockEntity::new, UNIVERSITY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LaboratoryBlockEntity>> LABORATORY_BE =
            BLOCK_ENTITIES.register("laboratory", () -> BlockEntityType.Builder.of(LaboratoryBlockEntity::new, LABORATORY.get()).build(null));

    public static void init() {}
}