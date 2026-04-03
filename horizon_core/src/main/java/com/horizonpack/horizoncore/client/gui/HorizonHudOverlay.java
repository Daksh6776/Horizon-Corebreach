package com.horizonpack.horizoncore.client.gui;

import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.client.ClientHorizonData;
import com.horizonpack.horizoncore.client.HorizonKeybinds;
import com.horizonpack.horizoncore.core.HorizonConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HorizonHudOverlay {

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null || mc.level == null) return;

        // 1. THE NEW SAFETY CHECK (Read directly from the player capability!)
        IHorizonPlayerData playerData = HorizonCapabilities.get(mc.player);
        if (playerData == null) {
            return; // Don't draw if the capability hasn't synced yet
        }

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // 2. RENDER THE HUD
        if (HorizonKeybinds.SHOW_DETAILS.isDown()) {
            renderDetailedNutrition(graphics, mc, height, playerData);
        } else {
            renderEnvironmentInfo(graphics, mc, width, playerData);
        }

        // 3. SKILL XP TOAST
        if (HorizonConfig.CLIENT.showSkillXPToasts.get() && ClientHorizonData.toastTicksRemaining > 0) {
            int textWidth = mc.font.width(ClientHorizonData.lastSkillToast);
            graphics.drawString(mc.font, ClientHorizonData.lastSkillToast, (width - textWidth) / 2, height / 4, 0x00FFFF, true);
            ClientHorizonData.toastTicksRemaining--;
        }
    }

    /**
     * DEFAULT VIEW: Top Right corner. Shows Era, Date & Time, Body Temp, World Temp, and Humidity.
     */
    private static void renderEnvironmentInfo(GuiGraphics graphics, Minecraft mc, int screenWidth, IHorizonPlayerData playerData) {
        int yOffset = 10;
        Level level = mc.level;
        BlockPos pos = mc.player.blockPosition();

        // --- 1. Era / Age ---
        if (HorizonConfig.CLIENT.showAgeNotification.get()) {
            String ageText = ClientHorizonData.getAge().getDisplayName();
            graphics.drawString(mc.font, ageText, screenWidth - mc.font.width(ageText) - 10, yOffset, 0xFFDF00, true);
            yOffset += 12;
        }

        // --- 2. Date & Time ---
        long totalTime = level.getDayTime();
        long day = (totalTime / 24000L) + 1; // 24,000 ticks in a Minecraft day
        long hours = ((totalTime / 1000L) + 6) % 24; // 0 ticks is 6:00 AM
        long minutes = (long) (((totalTime % 1000L) / 1000.0) * 60.0);
        String timeText = String.format("Day %d - %02d:%02d", day, hours, minutes);

        graphics.drawString(mc.font, timeText, screenWidth - mc.font.width(timeText) - 10, yOffset, 0xFFFFFF, true);
        yOffset += 12;

        // --- 3. Body Temperature (From Server Packet) ---
        if (HorizonConfig.CLIENT.showTemperatureHUD.get()) {
            float bodyTemp = playerData.getBodyTemperature();
            int tempColor = bodyTemp > 38.0f ? 0xFF5555 : (bodyTemp < 36.0f ? 0x5555FF : 0x55FF55);
            String tempText = String.format("Body Temp: %.1f \u00B0C", bodyTemp);

            graphics.drawString(mc.font, tempText, screenWidth - mc.font.width(tempText) - 10, yOffset, tempColor, true);
            yOffset += 12;
        }

        // --- 4. World Temperature & Humidity (Calculated on Client) ---
        Holder<Biome> biomeHolder = level.getBiome(pos);
        float vanillaTemp = biomeHolder.value().getBaseTemperature();
        boolean hasRain = biomeHolder.value().hasPrecipitation();

        // Convert Vanilla temp into Celsius
        float worldTempC = (vanillaTemp * 30.0F) - 5.0F;

        // Calculate realistic humidity based on rain and temperature
        int humidityPct;
        if (!hasRain) {
            // Deserts and Savannas are hot and dry, Snowy Tundras are cold and dry
            humidityPct = vanillaTemp > 0.8f ? 20 : 40;
        } else {
            // Jungles/Swamps are hot and humid, Forests/Plains are average
            humidityPct = vanillaTemp > 0.8f ? 85 : 60;
        }

        String worldEnvText = String.format("World: %.1f \u00B0C | Hum: %d%%", worldTempC, humidityPct);
        graphics.drawString(mc.font, worldEnvText, screenWidth - mc.font.width(worldEnvText) - 10, yOffset, 0x55FFFF, true);
    } // <--- THIS IS THE BRACKET THAT WAS MISSING!

    /**
     * TAB VIEW: Bottom Left corner. Compact GTNH-style nutrition bars.
     */
    private static void renderDetailedNutrition(GuiGraphics graphics, Minecraft mc, int screenHeight, IHorizonPlayerData playerData) {
        int startX = 10;
        int startY = screenHeight - 65;

        // Draw 5 compact bars stacked on top of each other
        drawCompactBar(graphics, mc, "PRO", playerData.getProtein(), 100f, startX, startY, 0xFFFF5555);
        drawCompactBar(graphics, mc, "CAR", playerData.getCarbohydrates(), 100f, startX, startY + 10, 0xFFFFAA00);
        drawCompactBar(graphics, mc, "FAT", playerData.getFats(), 100f, startX, startY + 20, 0xFFFFFF55);
        drawCompactBar(graphics, mc, "FIB", playerData.getFiber(), 100f, startX, startY + 30, 0xFF55FF55);
        drawCompactBar(graphics, mc, "H2O", playerData.getHydration(), 100f, startX, startY + 40, 0xFF5555FF);
    }

    /**
     * Helper method to draw a sleek, 1-pixel border health/nutrition bar.
     */
    private static void drawCompactBar(GuiGraphics graphics, Minecraft mc, String label, float value, float max, int x, int y, int color) {
        graphics.drawString(mc.font, label, x, y - 1, 0xAAAAAA, true);

        int barX = x + 24;
        int barWidth = 50;
        int barHeight = 6;

        // Draw background border (Dark Gray)
        graphics.fill(barX - 1, y - 1, barX + barWidth + 1, y + barHeight + 1, 0xFF222222);
        // Draw empty background (Darker inner)
        graphics.fill(barX, y, barX + barWidth, y + barHeight, 0xFF444444);

        int fillWidth = (int) ((value / max) * barWidth);
        fillWidth = Math.max(0, Math.min(fillWidth, barWidth));

        // Draw the colored fill
        graphics.fill(barX, y, barX + fillWidth, y + barHeight, color);
    }
}