package com.horizonpack.horizoncore.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkillBookItem extends Item {

    public SkillBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        // GUIs only exist on the Client (the player's actual computer),
        // not the Server. So we must check if we are on the Client side.
        if (level.isClientSide()) {
            openScreen();
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    // We isolate the screen code here with @OnlyIn so dedicated servers don't crash
    // when they try to read code meant for a graphics window.
    @OnlyIn(Dist.CLIENT)
    private void openScreen() {
        net.minecraft.client.Minecraft.getInstance().setScreen(new com.horizonpack.horizoncore.client.gui.SkillBookScreen());
    }
}