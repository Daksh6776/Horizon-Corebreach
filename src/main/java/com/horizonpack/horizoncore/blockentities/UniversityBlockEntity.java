package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UniversityBlockEntity extends BlockEntity {
    public UniversityBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.UNIVERSITY_BE.get(), pos, state);
    }
}