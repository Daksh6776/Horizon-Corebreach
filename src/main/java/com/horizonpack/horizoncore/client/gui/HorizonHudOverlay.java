package com.horizonpack.horizoncore.client.gui;

import com.horizonpack.horizoncore.client.ClientHorizonData;
import com.horizonpack.horizoncore.core.HorizonConfig;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HorizonHudOverlay {

    private static final ResourceLocation NUTRITION_BAR = ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "textures/gui/nutrition.png");

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null) return;

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // 1. Draw Age Badge (Top Right)
        if (HorizonConfig.CLIENT.showAgeNotification.get()) {
            String ageText = ClientHorizonData.currentAge.getDisplayName();
            int textWidth = mc.font.width(ageText);
            graphics.drawString(mc.font, ageText, width - textWidth - 10, 10, 0xFFDF00, true);
        }

        // 2. Draw Nutrition Bars (Bottom Left, above hotbar)
        if (HorizonConfig.CLIENT.showNutritionHUD.get()) {
            int startX = 10;
            int startY = height - 50;

            float protein = ClientHorizonData.playerData.getProtein();
            // In a full implementation, you would draw specific texture rects here based on the float values
            // Example stub:
            graphics.fill(startX, startY, startX + (int)(protein / 2), startY + 5, 0xFFFF0000); // Red bar for protein
            graphics.drawString(mc.font, "PRO", startX, startY - 10, 0xFFFFFF, true);
        }

        // 3. Draw Temperature (Next to Nutrition)
        if (HorizonConfig.CLIENT.showTemperatureHUD.get()) {
            float temp = ClientHorizonData.playerData.getBodyTemperature();
            int color = temp > 38.0f ? 0xFF0000 : (temp < 36.0f ? 0x0000FF : 0x00FF00); // Red hot, Blue cold, Green normal
            graphics.drawString(mc.font, String.format("%.1f °C", temp), 10, height - 70, color, true);
        }

        // 4. Draw Skill XP Toast
        if (HorizonConfig.CLIENT.showSkillXPToasts.get() && ClientHorizonData.toastTicksRemaining > 0) {
            int textWidth = mc.font.width(ClientHorizonData.lastSkillToast);
            graphics.drawString(mc.font, ClientHorizonData.lastSkillToast, (width - textWidth) / 2, height / 4, 0x00FFFF, true);
            ClientHorizonData.toastTicksRemaining--; // Note: proper decrementing should happen in a client tick event, but this works for testing rendering
        }
    }
}