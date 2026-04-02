package com.horizonpack.horizoncore.client;

import com.horizonpack.horizoncore.client.HorizonKeybinds;
import com.horizonpack.horizoncore.client.gui.HorizonHudOverlay;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.client.render.HorizonBlockEntityRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;


@EventBusSubscriber(modid = HorizonCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class HorizonClientEvents {

    @SubscribeEvent
    public static void registerGuiLayers(@NotNull RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "hud_overlay"),
                HorizonHudOverlay::render);
    }

    // Add this new event to register the keybind!
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(HorizonKeybinds.SHOW_DETAILS);
    }

    // This is now safely inside the class!
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(com.horizonpack.horizoncore.core.HorizonRegistries.RESEARCH_BENCH_MENU.get(),
                com.horizonpack.horizoncore.client.gui.ResearchBenchScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HorizonRegistries.RESEARCH_BENCH_BE.get(), HorizonBlockEntityRenderer::new);
    }
}