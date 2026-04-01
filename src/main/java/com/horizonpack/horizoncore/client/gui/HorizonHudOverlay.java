package com.horizonpack.horizoncore.client.gui;

import com.horizonpack.horizoncore.client.ClientHorizonData;
import com.horizonpack.horizoncore.client.HorizonKeybinds;
import com.horizonpack.horizoncore.core.HorizonConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HorizonHudOverlay {

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null || mc.level == null) return;

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // Check if our custom key (TAB) is held down
        if (HorizonKeybinds.SHOW_DETAILS.isDown()) {
            renderDetailedNutrition(graphics, mc, height);
        } else {
            renderEnvironmentInfo(graphics, mc, width);
        }

        // Draw Skill XP Toast (Always show this if active)
        if (HorizonConfig.CLIENT.showSkillXPToasts.get() && ClientHorizonData.toastTicksRemaining > 0) {
            int textWidth = mc.font.width(ClientHorizonData.lastSkillToast);
            graphics.drawString(mc.font, ClientHorizonData.lastSkillToast, (width - textWidth) / 2, height / 4, 0x00FFFF, true);
            ClientHorizonData.toastTicksRemaining--;
        }
    }

    /**
     * DEFAULT VIEW: Top Right corner. Shows Age, Time, Temp, and Humidity.
     */
    private static void renderEnvironmentInfo(GuiGraphics graphics, Minecraft mc, int screenWidth) {
        int yOffset = 10;

        // 1. Era / Age
        if (HorizonConfig.CLIENT.showAgeNotification.get()) {
            String ageText = ClientHorizonData.getAge().getDisplayName();
            graphics.drawString(mc.font, ageText, screenWidth - mc.font.width(ageText) - 10, yOffset, 0xFFDF00, true);
            yOffset += 12;
        }

        // 2. In-Game Time (Formatted 24hr)
        long time = mc.level.getDayTime() % 24000;
        long hours = ((time / 1000) + 6) % 24; // Minecraft 0 ticks is 6 AM
        long minutes = (time % 1000) * 60 / 1000;
        String timeText = String.format("Time: %02d:%02d", hours, minutes);
        graphics.drawString(mc.font, timeText, screenWidth - mc.font.width(timeText) - 10, yOffset, 0xFFFFFF, true);
        yOffset += 12;

        // 3. Body Temperature
        if (HorizonConfig.CLIENT.showTemperatureHUD.get()) {
            float temp = ClientHorizonData.playerData.getBodyTemperature();
            int tempColor = temp > 38.0f ? 0xFF5555 : (temp < 36.0f ? 0x5555FF : 0x55FF55);
            String tempText = String.format("Temp: %.1f \u00B0C", temp);
            graphics.drawString(mc.font, tempText, screenWidth - mc.font.width(tempText) - 10, yOffset, tempColor, true);
            yOffset += 12;
        }

        // 4. Local Humidity (Mocked based on Biome temp for now)
        BlockPos pos = mc.player.blockPosition();
        float biomeTemp = mc.level.getBiome(pos).value().getBaseTemperature();
        int humidity = biomeTemp > 0.85f ? 20 : (biomeTemp < 0.2f ? 40 : 65); // Placeholder math until we build the weather system
        String humText = String.format("Humidity: %d%%", humidity);
        graphics.drawString(mc.font, humText, screenWidth - mc.font.width(humText) - 10, yOffset, 0x55FFFF, true);
    }

    /**
     * TAB VIEW: Bottom Left corner. Compact GTNH-style nutrition bars.
     */
    private static void renderDetailedNutrition(GuiGraphics graphics, Minecraft mc, int screenHeight) {
        int startX = 10;
        int startY = screenHeight - 65; // Just above the chat window

        // Draw 5 compact bars stacked on top of each other
        drawCompactBar(graphics, mc, "PRO", ClientHorizonData.playerData.getProtein(), 100f, startX, startY, 0xFFFF5555);
        drawCompactBar(graphics, mc, "CAR", ClientHorizonData.playerData.getCarbohydrates(), 100f, startX, startY + 10, 0xFFFFAA00);
        drawCompactBar(graphics, mc, "FAT", ClientHorizonData.playerData.getFats(), 100f, startX, startY + 20, 0xFFFFFF55);
        drawCompactBar(graphics, mc, "FIB", ClientHorizonData.playerData.getFiber(), 100f, startX, startY + 30, 0xFF55FF55);
        drawCompactBar(graphics, mc, "H2O", ClientHorizonData.playerData.getHydration(), 100f, startX, startY + 40, 0xFF5555FF);
    }

    /**
     * Helper method to draw a sleek, 1-pixel border health/nutrition bar.
     */
    private static void drawCompactBar(GuiGraphics graphics, Minecraft mc, String label, float value, float max, int x, int y, int color) {
        // Draw the 3-letter label (e.g. "PRO")
        graphics.drawString(mc.font, label, x, y - 1, 0xAAAAAA, true);

        int barX = x + 24; // Offset past the text
        int barWidth = 50; // Total width of the bar
        int barHeight = 6; // Total height of the bar

        // Draw background border (Dark Gray)
        graphics.fill(barX - 1, y - 1, barX + barWidth + 1, y + barHeight + 1, 0xFF222222);
        // Draw empty background (Darker inner)
        graphics.fill(barX, y, barX + barWidth, y + barHeight, 0xFF444444);

        // Calculate how wide the filled portion should be
        int fillWidth = (int) ((value / max) * barWidth);
        fillWidth = Math.max(0, Math.min(fillWidth, barWidth)); // Clamp between 0 and 50

        // Draw the colored fill
        graphics.fill(barX, y, barX + fillWidth, y + barHeight, color);
    }
}