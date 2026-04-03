package com.horizonpack.horizondata.items;

import com.horizonpack.horizondata.data.MaterialDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;

public class GearItem extends Item {
    private final MaterialDefinition material;

    public GearItem(Properties properties, MaterialDefinition material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Material: ").append(Component.literal(material.getMaterialId()).withStyle(ChatFormatting.AQUA)));
        // Logic: Harder materials make for higher "Gear Tiers"
        String tier = material.getHardness() > 4.0f ? "High Performance" : "Standard";
        tooltip.add(Component.literal("Tier: " + tier).withStyle(ChatFormatting.DARK_AQUA));
    }
}