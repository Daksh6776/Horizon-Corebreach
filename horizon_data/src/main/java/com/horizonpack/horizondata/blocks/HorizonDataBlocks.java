package com.horizonpack.horizondata.blocks;

import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.function.Supplier;

public class HorizonDataBlocks {

    // --- Surface Rocks (Stone Age) ---
    public static final Supplier<Block> LOOSE_PEBBLES = registerBlock("loose_pebbles",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().sound(SoundType.STONE).noCollission()));

    // --- Ores (Copper/Bronze Age) ---
    //public static final Supplier<Block> TIN_ORE = registerBlock("tin_ore",
     //       () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f)));
    //public static final Supplier<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
          //  () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE)));

    //public static final Supplier<Block> ZINC_ORE = registerBlock("zinc_ore",
           // () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f)));
    //public static final Supplier<Block> DEEPSLATE_ZINC_ORE = registerBlock("deepslate_zinc_ore",
          //  () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE)));

    private static Supplier<Block> registerBlock(String name, Supplier<Block> block) {
        return DataRegistries.BLOCKS.register(name, block);
    }
}