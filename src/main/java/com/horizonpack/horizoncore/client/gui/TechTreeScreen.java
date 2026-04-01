package com.horizonpack.horizoncore.client.gui;

import com.horizonpack.horizoncore.core.HorizonCore;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TechTreeScreen extends Screen {

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(HorizonCore.MODID, "textures/gui/tech_tree_bg.png");
    private double scrollX = 0;
    private double scrollY = 0;

    public TechTreeScreen() {
        super(Component.translatable("gui.horizoncore.tech_tree.title"));
    }

    @Override
    protected void init() {
        super.init();
        // Here you would parse TechnologyGraph.java and add Renderable buttons for each Tech node
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        // Draw the panning background
        // graphics.blit(BG_TEXTURE, ...);

        // Draw connecting lines between prerequisites and dependents

        super.render(graphics, mouseX, mouseY, partialTick);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) { // Left click pan
            this.scrollX += dragX;
            this.scrollY += dragY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}