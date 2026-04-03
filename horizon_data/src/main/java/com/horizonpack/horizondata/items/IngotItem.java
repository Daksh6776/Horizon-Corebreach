package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.data.MaterialDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;

public class IngotItem extends Item {
    private final MaterialDefinition material;

    public IngotItem(Properties properties, MaterialDefinition material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Material: ").append(Component.literal(material.getMaterialId()).withStyle(ChatFormatting.GOLD)));
        tooltip.add(Component.literal("Density: " + material.getDensity()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Melting Point: " + material.getMeltingPoint() + "°C").withStyle(ChatFormatting.GRAY));
        if (material.isMetal()) {
            tooltip.add(Component.literal("Pure Metal").withStyle(ChatFormatting.BLUE));
        }
    }

    public MaterialDefinition getMaterial() { return material; }
}