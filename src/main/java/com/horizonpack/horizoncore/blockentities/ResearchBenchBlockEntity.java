package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ResearchBenchBlockEntity extends BlockEntity {
    public ResearchBenchBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.RESEARCH_BENCH_BE.get(), pos, state);
    }
}