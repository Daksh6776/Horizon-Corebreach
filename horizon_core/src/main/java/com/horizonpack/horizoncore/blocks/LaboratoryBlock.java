package com.horizonpack.horizoncore.blocks;

import com.horizonpack.horizoncore.blockentities.LaboratoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaboratoryBlock extends Block implements EntityBlock {
    public LaboratoryBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LaboratoryBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider container) {
                // This call triggers the createMenu method in the LaboratoryBlockEntity
                player.openMenu(container, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}