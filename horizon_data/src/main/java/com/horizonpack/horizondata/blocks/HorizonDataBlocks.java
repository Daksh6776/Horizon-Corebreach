package com.horizonpack.horizondata.blocks;

import com.horizonpack.horizondata.blocks.workshops.*;
import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.function.Supplier;
import com.horizonpack.horizondata.blocks.deposits.GasDepositBlock;
import com.horizonpack.horizondata.blocks.deposits.OilDepositBlock;

public class HorizonDataBlocks {

    // --- Surface Rocks (Stone Age) ---
    // Keep this here because the automated Ore Registry doesn't handle "pebbles"
    public static final Supplier<Block> LOOSE_PEBBLES = registerBlock("loose_pebbles",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().sound(SoundType.STONE).noCollission()));

    // --- Basic Workshops ---
    public static final Supplier<Block> BLOOMERY = registerBlock("bloomery",
            () -> new BloomeryBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5f)));

    public static final Supplier<Block> CRUCIBLE = registerBlock("crucible",
            () -> new CrucibleBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5f)));

    public static final Supplier<Block> GRINDSTONE = registerBlock("grindstone",
            () -> new GrindstoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5f)));

    public static final Supplier<Block> FORGE = registerBlock("forge",
            () -> new ForgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.5f)));

    // --- Advanced Workshops ---
    public static final Supplier<Block> ALCHEMY_LAB = registerBlock("alchemy_lab",
            () -> new AlchemyLabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5f)));

    public static final Supplier<Block> ANVIL = registerBlock("anvil",
            () -> new AnvilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0f)));

    public static final Supplier<Block> CARPENTRY_BENCH = registerBlock("carpentry_bench",
            () -> new CarpentryBenchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f)));

    public static final Supplier<Block> KITCHEN = registerBlock("kitchen",
            () -> new KitchenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.5f)));

    public static final Supplier<Block> LOOM = registerBlock("loom",
            () -> new LoomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f)));

    public static final Supplier<Block> POTTERY_WHEEL = registerBlock("pottery_wheel",
            () -> new PotteryWheelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f)));

    public static final Supplier<Block> TANNERY = registerBlock("tannery",
            () -> new TanneryBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f)));

    public static final Supplier<Block> WORKBENCH = registerBlock("workbench",
            () -> new WorkbenchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f)));

    // Helper method to register blocks via your DeferredRegister
    private static Supplier<Block> registerBlock(String name, Supplier<Block> block) {
        return DataRegistries.BLOCKS.register(name, block);
    }
    // --- Fluid Deposits ---
    public static final Supplier<Block> OIL_DEPOSIT = registerBlock("oil_deposit",
            () -> new OilDepositBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0f)));

    // Notice the registry name is "natural_gas_pocket" but the variable is GAS_DEPOSIT
    public static final Supplier<Block> GAS_DEPOSIT = registerBlock("natural_gas_pocket",
            () -> new GasDepositBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0f)));

    public static void init() { /* forces static field initialization */ }
}