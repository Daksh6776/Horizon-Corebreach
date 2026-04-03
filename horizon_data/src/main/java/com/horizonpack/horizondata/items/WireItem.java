package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.data.MaterialDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;

public class WireItem extends Item {
    private final MaterialDefinition material;

    public WireItem(Properties properties, MaterialDefinition material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Conductivity: ").append(Component.literal(String.format("%.2f", material.getConductivity())).withStyle(ChatFormatting.YELLOW)));
        if (!material.isConductor()) {
            tooltip.add(Component.literal("Non-Conductive").withStyle(ChatFormatting.RED));
        }
    }
}