package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.data.MaterialDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;

public class PlateItem extends Item {
    private final MaterialDefinition material;

    public PlateItem(Properties properties, MaterialDefinition material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Reinforced ").append(Component.literal(material.getMaterialId() + " Plate").withStyle(ChatFormatting.WHITE)));
    }
}