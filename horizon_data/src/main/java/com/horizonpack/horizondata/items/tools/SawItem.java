package com.horizonpack.horizondata.items.tools;

import com.horizonpack.horizondata.blocks.workshops.CarpentryBenchBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SawItem extends Item {
    public SawItem(Properties properties, String materialName) {
        super(properties.durability(200));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Player player = context.getPlayer();

        if (player != null && state.getBlock() instanceof CarpentryBenchBlock bench) {
            // Trigger the bench UI opening logic
            return bench.useWithoutItem(state, level, context.getClickedPos(), player, null);
        }

        return InteractionResult.PASS;
    }
}