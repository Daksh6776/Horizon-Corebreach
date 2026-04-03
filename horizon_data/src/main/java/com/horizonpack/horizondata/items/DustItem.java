package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.data.MaterialDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;

public class DustItem extends Item {
    private final MaterialDefinition material;

    public DustItem(Properties properties, MaterialDefinition material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Powdered ").append(Component.literal(material.getMaterialId()).withStyle(ChatFormatting.GOLD)));
        tooltip.add(Component.literal("Can be smelted into an Ingot.").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }
}