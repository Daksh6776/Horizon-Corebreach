package com.horizonpack.horizoncore.data;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class OreEvolutionManager {
    public static void tickEvolution(ServerLevel level, WorldAgeData data) {
        HorizonAge age = data.getGlobalAge();
        if (age == HorizonAge.STONE_AGE) return;

        level.players().forEach(player -> {
            BlockPos pPos = player.blockPosition();
            RandomSource rand = level.random;

            // Only attempt formation in a 32x32 area around player
            for (int i = 0; i < 5; i++) {
                int x = pPos.getX() + rand.nextInt(-16, 16);
                int z = pPos.getZ() + rand.nextInt(-16, 16);

                // Get Y-range from our progression rules
                int[] range = OreProgression.getYRangeForAge(age);
                int y = rand.nextInt(range[0], range[1]);

                BlockPos target = new BlockPos(x, y, z);
                if (level.getBlockState(target).is(Blocks.STONE) || level.getBlockState(target).is(Blocks.DEEPSLATE)) {
                    level.setBlockAndUpdate(target, getOre(age, level.getBlockState(target).is(Blocks.DEEPSLATE)));
                }
            }
        });
    }

    private static BlockState getOre(HorizonAge age, boolean deepslate) {
        if (age == HorizonAge.COPPER_AGE) return Blocks.COPPER_ORE.defaultBlockState();
        if (age == HorizonAge.IRON_AGE) return deepslate ? Blocks.DEEPSLATE_IRON_ORE.defaultBlockState() : Blocks.IRON_ORE.defaultBlockState();
        return Blocks.COAL_ORE.defaultBlockState();
    }
}