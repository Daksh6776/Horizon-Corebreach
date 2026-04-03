package com.horizonpack.horizondata.blocks.workshops;

import com.horizonpack.horizondata.blockentities.CarpentryBenchBlockEntity;
import com.horizonpack.horizondata.inventory.CarpentryBenchMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CarpentryBenchBlock extends BaseEntityBlock {
    public static final MapCodec<CarpentryBenchBlock> CODEC = simpleCodec(CarpentryBenchBlock::new);

    public CarpentryBenchBlock(Properties properties) { super(properties); }

    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new CarpentryBenchBlockEntity(pos, state); }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            // ... (existing logic to open menu)
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}