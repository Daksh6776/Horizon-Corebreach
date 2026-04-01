package com.horizonpack.horizoncore.client;

import com.horizonpack.horizoncore.client.gui.HorizonHudOverlay;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

// Removed the "Mod." prefix from the annotation and the Bus type
@EventBusSubscriber(modid = HorizonCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class HorizonClientEvents {

    @SubscribeEvent
    public static void registerGuiLayers(@NotNull RegisterGuiLayersEvent event) {
        // Registers our custom HUD overlay to render just above the hotbar layer
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "hud_overlay"),
                HorizonHudOverlay::render);
    }
}