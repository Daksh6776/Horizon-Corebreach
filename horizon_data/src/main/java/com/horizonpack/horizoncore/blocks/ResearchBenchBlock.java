package com.horizonpack.horizoncore.blocks;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.blockentities.ResearchBenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ResearchBenchBlock extends Block implements EntityBlock {

    public ResearchBenchBlock(Properties properties) {
        super(properties);
    }

    // 1. Creates the Block Entity (The Brain) when the block is placed [cite: 71, 213]
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResearchBenchBlockEntity(pos, state);
    }

    // 2. Registers the Tick method so the research timer actually counts down [cite: 196-198]
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // We only want to run the research timer on the Server side
        if (level.isClientSide()) {
            return null;
        }

        // This links the Block to the tick() method we wrote in ResearchBenchBlockEntity
        return (l, p, s, be) -> {
            if (be instanceof ResearchBenchBlockEntity bench) {
                ResearchBenchBlockEntity.tick(l, p, s, bench);
            }
        };
    }

    // 3. Opens the UI when a player right-clicks the block (NeoForge 1.21.1 standard)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ResearchBenchBlockEntity) {
                // Assuming ResearchBenchBlockEntity implements MenuProvider
                player.openMenu((net.minecraft.world.MenuProvider) blockEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}