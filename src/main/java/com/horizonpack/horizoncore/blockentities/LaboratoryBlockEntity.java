package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LaboratoryBlockEntity extends BlockEntity {
    public LaboratoryBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.LABORATORY_BE.get(), pos, state);
    }
}