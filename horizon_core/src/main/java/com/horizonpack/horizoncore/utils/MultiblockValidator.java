package com.horizonpack.horizoncore.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockValidator {

    /**
     * Scans the world to verify a 3x3x4 University structure.
     * @param level The current world.
     * @param centerBottom The center floor block (Y=0) of the 3x3 area.
     * @return true if the structure is perfectly built.
     */
    public static boolean isUniversityValid(Level level, BlockPos centerBottom) {
        // Scan a 3x3 grid (X and Z from -1 to 1) and 4 blocks tall (Y from 0 to 3)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 3; y++) {
                    BlockPos checkPos = centerBottom.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    // 1. The Floor (Y=0) and Roof (Y=3) must be entirely Stone Bricks
                    if (y == 0 || y == 3) {
                        if (!state.is(Blocks.STONE_BRICKS)) return false;
                    }
                    // 2. The Walls (Y=1 and Y=2)
                    else {
                        // The exact center columns (where the player stands/interacts) can be air or furniture
                        if (x == 0 && z == 0) {
                            continue;
                        }
                        // The 4 Corners must be Stone Bricks (Pillars)
                        if (Math.abs(x) == 1 && Math.abs(z) == 1) {
                            if (!state.is(Blocks.STONE_BRICKS)) return false;
                        }
                        // The edges between the corners must be Bookshelves
                        else {
                            if (!state.is(Blocks.BOOKSHELF)) return false;
                        }
                    }
                }
            }
        }
        return true; // If the loop finishes without returning false, the structure is perfect!
    }
}