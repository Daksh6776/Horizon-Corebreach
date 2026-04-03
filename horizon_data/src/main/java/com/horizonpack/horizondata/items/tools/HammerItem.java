package com.horizonpack.horizondata.items.tools;

import com.horizonpack.horizondata.blocks.workshops.AnvilBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends Item {
    public HammerItem(Properties properties, String materialName) {
        super(properties.durability(250));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Player player = context.getPlayer();

        // Safety check for null player to satisfy the IDE warnings
        if (player != null && state.getBlock() instanceof AnvilBlock anvil) {
            // This now works because we made the method public in AnvilBlock
            return anvil.useWithoutItem(state, level, context.getClickedPos(), player, context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? null : null);
        }

        return InteractionResult.PASS;
    }
}