package com.horizonpack.horizoncore.client;

import com.horizonpack.horizoncore.client.gui.HorizonHudOverlay;
import com.horizonpack.horizoncore.core.HorizonCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod.EventBusSubscriber(modid = HorizonCore.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HorizonClientEvents {

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        // Registers our custom HUD overlay to render just above the hotbar layer
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                new net.minecraft.resources.ResourceLocation(HorizonCore.MODID, "hud_overlay"),
                HorizonHudOverlay::render);
    }
}