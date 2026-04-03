package com.horizonpack.horizoncore.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class OreProgression {
    public static boolean canPlayerSee(Block block, HorizonAge age) {
        if (block == Blocks.COPPER_ORE) return age.ordinal() >= HorizonAge.COPPER_AGE.ordinal();
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) return age.ordinal() >= HorizonAge.IRON_AGE.ordinal();
        return true;
    }

    public static boolean canPlayerMine(Block block, HorizonAge age) {
        return canPlayerSee(block, age);
    }

    public static int[] getYRangeForAge(HorizonAge age) {
        return new int[]{-64, 320}; // Placeholder
    }
}