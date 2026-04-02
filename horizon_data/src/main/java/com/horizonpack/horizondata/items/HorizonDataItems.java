package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.blocks.HorizonDataBlocks;
import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class HorizonDataItems {

    // --- Block Items ---
    // Keep loose pebbles as it is not part of the automatic Ore loop
    public static final Supplier<Item> LOOSE_PEBBLES_ITEM = registerBlockItem("loose_pebbles", HorizonDataBlocks.LOOSE_PEBBLES);

    // ORE BLOCK ITEMS REMOVED: They are now handled automatically by OreBlockRegistry

    // --- Raw Materials & Ingots ---
    public static final Supplier<Item> RAW_TIN = registerItem("raw_tin", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_ZINC = registerItem("raw_zinc", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TIN_INGOT = registerItem("tin_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ZINC_INGOT = registerItem("zinc_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BRONZE_INGOT = registerItem("bronze_ingot", () -> new Item(new Item.Properties()));

    private static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return DataRegistries.ITEMS.register(name, item);
    }

    private static Supplier<Item> registerBlockItem(String name, Supplier<net.minecraft.world.level.block.Block> block) {
        return DataRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}