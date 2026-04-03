package com.horizonpack.horizoncore.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TechNodeButton extends AbstractButton {

    private final ResourceLocation techId;
    private final ResourceLocation icon;
    private final boolean isUnlocked;
    private final boolean isAvailable;
    private final TechTreeScreen parentScreen;

    public TechNodeButton(int x, int y, ResourceLocation techId, ResourceLocation icon, boolean isUnlocked, boolean isAvailable, TechTreeScreen parentScreen) {
        // Buttons are 32x32 pixels
        super(x, y, 32, 32, Component.literal(techId.getPath()));
        this.techId = techId;
        this.icon = icon;
        this.isUnlocked = isUnlocked;
        this.isAvailable = isAvailable;
        this.parentScreen = parentScreen;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 1. Determine Border Color
        int borderColor = 0xFF888888; // Default Gray (Locked)
        if (isUnlocked) borderColor = 0xFF55FF55; // Green (Unlocked)
        else if (isAvailable) borderColor = 0xFFFFFF55; // Yellow (Available)

        // 2. Draw the Box
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF222222); // Dark BG
        graphics.renderOutline(this.getX(), this.getY(), this.width, this.height, borderColor); // Colored Border

        // 3. Draw the Icon (if valid)
        if (icon != null && !icon.getPath().isEmpty()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.isUnlocked || this.isAvailable ? 1.0F : 0.3F);
            graphics.blit(icon, this.getX() + 8, this.getY() + 8, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        // 4. Handle Tooltips on Hover
        if (this.isHovered) {
            graphics.renderTooltip(net.minecraft.client.Minecraft.getInstance().font, Component.translatable("tech." + techId.getNamespace() + "." + techId.getPath()), mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
        // When clicked, tell the parent screen to open the detail panel for this tech
        parentScreen.setSelectedTech(this.techId, this.isUnlocked, this.isAvailable);
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {}
}