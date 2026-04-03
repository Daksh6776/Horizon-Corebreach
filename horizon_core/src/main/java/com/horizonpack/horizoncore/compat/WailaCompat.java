package com.horizonpack.horizoncore.compat;

import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // Register the Age requirement tooltip to show on all blocks
        // You can restrict this to specific block classes if needed
        registration.registerBlockComponent(AgeRequirementProvider.INSTANCE, Block.class);
    }

    public static class AgeRequirementProvider implements IBlockComponentProvider {
        public static final AgeRequirementProvider INSTANCE = new AgeRequirementProvider();

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            // Placeholder: Replace with your actual logic to check the block's required age
            // Example: String requiredAge = AgeSystem.getRequiredAge(accessor.getBlock());
            String requiredAge = "Stone Age";

            tooltip.add(Component.literal("Age Requirement: ").append(Component.literal(requiredAge)));
        }

        @Override
        public ResourceLocation getUid() {
            return ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "age_requirement");
        }
    }
}