package com.horizonpack.horizoncore.blocks;

import com.horizonpack.horizoncore.blockentities.UniversityBlockEntity;
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

public class UniversityBlock extends Block implements EntityBlock {
    public UniversityBlock(Properties properties) {
        super(properties);
    }

    // FIXED: Added the required method to create the Block Entity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new UniversityBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, Level level, BlockPos pos, Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider container) {
                // Opens the UI defined in UniversityBlockEntity
                player.openMenu(container, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}