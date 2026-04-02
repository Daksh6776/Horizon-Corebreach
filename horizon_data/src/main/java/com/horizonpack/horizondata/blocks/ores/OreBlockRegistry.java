package com.horizonpack.horizondata.blocks.ores;

import com.horizonpack.horizoncore.data.HorizonAge;
import com.horizonpack.horizondata.data.OreDefinition;
import java.util.ArrayList;
import java.util.List;

public class OreBlockRegistry {

    // 1. Declare the list FIRST so it exists in memory
    public static final List<OreDefinition> ALL_ORES = new ArrayList<>();

    // 2. Define the ores and add them to the list using the helper method below
    public static final OreDefinition TIN = registerDef(new OreDefinition(
            "tin", 10, 12, -32, 64, HorizonAge.COPPER_AGE));

    public static final OreDefinition ZINC = registerDef(new OreDefinition(
            "zinc", 8, 8, -64, 32, HorizonAge.COPPER_AGE));

    // ─── CORE METALS & MINERALS ────────────────────────────────────────────────
    public static final OreDefinition IRON = registerDef(new OreDefinition(
            "iron", 12, 12, -64, 72, HorizonAge.STONE_AGE));

    public static final OreDefinition COPPER = registerDef(new OreDefinition(
            "copper", 16, 16, -16, 96, HorizonAge.STONE_AGE));

    public static final OreDefinition COAL = registerDef(new OreDefinition(
            "coal", 16, 20, 0, 256, HorizonAge.STONE_AGE));

    public static final OreDefinition GOLD = registerDef(new OreDefinition(
            "gold", 8, 2, -64, 32, HorizonAge.COPPER_AGE));

    public static final OreDefinition LEAD = registerDef(new OreDefinition(
            "lead", 8, 8, -32, 64, HorizonAge.COPPER_AGE));

    public static final OreDefinition SILVER = registerDef(new OreDefinition(
            "silver", 8, 4, -64, 32, HorizonAge.INDUSTRIAL));

    public static final OreDefinition NICKEL = registerDef(new OreDefinition(
            "nickel", 8, 6, -32, 64, HorizonAge.INDUSTRIAL));

    public static final OreDefinition ALUMINUM = registerDef(new OreDefinition(
            "aluminum", 8, 6, 0, 128, HorizonAge.INDUSTRIAL));

    // ─── PRECIOUS GEMS & MISC ──────────────────────────────────────────────────
    public static final OreDefinition DIAMOND = registerDef(new OreDefinition(
            "diamond", 4, 2, -64, 16, HorizonAge.IRON_AGE));

    public static final OreDefinition EMERALD = registerDef(new OreDefinition(
            "emerald", 3, 5, -16, 256, HorizonAge.IRON_AGE));

    public static final OreDefinition RUBY = registerDef(new OreDefinition(
            "ruby", 4, 2, -64, 32, HorizonAge.IRON_AGE));

    public static final OreDefinition SAPPHIRE = registerDef(new OreDefinition(
            "sapphire", 4, 2, -64, 32, HorizonAge.IRON_AGE));

    public static final OreDefinition SALT = registerDef(new OreDefinition(
            "salt", 12, 4, 32, 128, HorizonAge.STONE_AGE));

    // ─── ADVANCED INDUSTRIAL & MODERN ORES ─────────────────────────────────────
    public static final OreDefinition COBALT = registerDef(new OreDefinition(
            "cobalt", 6, 4, -64, 0, HorizonAge.MODERN_AGE));

    public static final OreDefinition CHROMIUM = registerDef(new OreDefinition(
            "chromium", 6, 4, -64, 0, HorizonAge.MODERN_AGE));

    public static final OreDefinition MANGANESE = registerDef(new OreDefinition(
            "manganese", 6, 4, -32, 32, HorizonAge.MODERN_AGE));

    public static final OreDefinition PLATINUM = registerDef(new OreDefinition(
            "platinum", 4, 2, -64, -16, HorizonAge.MODERN_AGE));

    public static final OreDefinition TITANIUM = registerDef(new OreDefinition(
            "titanium", 4, 3, -64, -16, HorizonAge.MODERN_AGE));

    public static final OreDefinition TUNGSTEN = registerDef(new OreDefinition(
            "tungsten", 4, 3, -64, -16, HorizonAge.MODERN_AGE));

    public static final OreDefinition URANIUM = registerDef(new OreDefinition(
            "uranium", 4, 2, -64, -32, HorizonAge.MODERN_AGE));

    public static final OreDefinition RARE_EARTH = registerDef(new OreDefinition(
            "rare_earth", 4, 2, -64, -32, HorizonAge.MODERN_AGE));

    // ─── FLUID DEPOSITS ────────────────────────────────────────────────────────
    // Note: If your registration loop automatically adds "_ore" to the end of the name, 
    // you may need to add an 'if' statement in your static block to handle these two separately, 
    // because the JSON files are looking for exact names: "oil_deposit" and "natural_gas_pocket".
    public static final OreDefinition OIL = registerDef(new OreDefinition(
            "oil_deposit", 16, 2, -64, 32, HorizonAge.MODERN_AGE));

    public static final OreDefinition GAS = registerDef(new OreDefinition(
            "natural_gas_pocket", 16, 2, -64, 32, HorizonAge.MODERN_AGE));

    // Helper method to add the definition to the list
    private static OreDefinition registerDef(OreDefinition def) {
        ALL_ORES.add(def);
        return def;
    }

    // 3. The init method you will call from HorizonData's constructor
    public static void init() {
        // Calling this forces Java to load the class, which triggers the static block below
    }

    // 4. The static block that actually registers the NeoForge blocks
    // This MUST be placed at the bottom so ALL_ORES is fully populated before it runs.
    static {
        for (OreDefinition def : ALL_ORES) {
            String name = def.name();

            // 1. Determine the block ID
            // We automatically add "_ore" unless it's already a "deposit" or "pocket"
            String stoneId = (name.contains("deposit") || name.contains("pocket")) ? name : name + "_ore";
            String deepslateId = "deepslate_" + stoneId;

            // 2. Register the Stone Version of the Block
            var stoneBlock = com.horizonpack.horizondata.core.DataRegistries.BLOCKS.register(stoneId,
                    () -> new net.minecraft.world.level.block.Block(
                            net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                                    .mapColor(net.minecraft.world.level.material.MapColor.STONE)
                                    .requiresCorrectToolForDrops()
                                    .strength(3.0F, 3.0F)
                    )
            );

            // 3. Register the Deepslate Version of the Block
            var deepslateBlock = com.horizonpack.horizondata.core.DataRegistries.BLOCKS.register(deepslateId,
                    () -> new net.minecraft.world.level.block.Block(
                            net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                                    .mapColor(net.minecraft.world.level.material.MapColor.DEEPSLATE)
                                    .requiresCorrectToolForDrops()
                                    .strength(4.5F, 3.0F)
                    )
            );

            // 4. Register the Items (so you can see them in your inventory)
            com.horizonpack.horizondata.core.DataRegistries.ITEMS.register(stoneId,
                    () -> new net.minecraft.world.item.BlockItem(stoneBlock.get(), new net.minecraft.world.item.Item.Properties())
            );
            com.horizonpack.horizondata.core.DataRegistries.ITEMS.register(deepslateId,
                    () -> new net.minecraft.world.item.BlockItem(deepslateBlock.get(), new net.minecraft.world.item.Item.Properties())
            );
        }
    }
}