package com.horizonpack.horizoncore.utils;

import net.minecraft.core.BlockPos;

public class HorizonMath {

    /** Linear interpolation for smooth UI animations */
    public static float lerp(float start, float end, float delta) {
        return start + delta * (end - start);
    }

    /** Fast squared distance check (avoids expensive Math.sqrt) */
    public static boolean isWithinRadius(BlockPos center, BlockPos target, double radius) {
        return center.distSqr(target) <= (radius * radius);
    }
}