package com.horizonpack.horizoncore.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import com.horizonpack.horizoncore.client.ClientHorizonData;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.data.TechnologyData;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import com.horizonpack.horizoncore.network.packets.RequestResearchPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.core.BlockPos;

@OnlyIn(Dist.CLIENT)
public class TechTreeScreen extends Screen {
    private double scrollX = 0;
    private double scrollY = 0;
    private ResourceLocation selectedTech = null;
    private Button beginResearchButton;

    protected TechTreeScreen(Component title) {
        super(title);
    }

    // ... (Keep your nodePositions map and constructor) ...

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        // 1. Create the "Begin Research" button first (hidden by default)
        this.beginResearchButton = Button.builder(Component.translatable("gui.horizoncore.start_research"), button -> {
            if (this.selectedTech != null) {
                // Send packet to server to start research! [cite: 341, 349, 356]
                BlockPos benchPos = this.minecraft.player.blockPosition(); // Assuming they are at the bench
                HorizonPacketHandler.sendToServer(new RequestResearchPacket(this.selectedTech, benchPos));
                this.minecraft.setScreen(null); // Close the GUI
            }
        }).bounds(this.width - 150, this.height - 40, 140, 20).build();

        this.beginResearchButton.visible = false;
        this.addRenderableWidget(this.beginResearchButton);

        // ... (Keep your existing node generation loop here) ...
    }

    // Update your setSelectedTech method
    public void setSelectedTech(ResourceLocation techId, boolean isUnlocked, boolean isAvailable) {
        this.selectedTech = techId;

        // Only show the button if they clicked a valid tech
        this.beginResearchButton.visible = true;

        // They can only click "Begin" if it's available and NOT already unlocked
        this.beginResearchButton.active = isAvailable && !isUnlocked;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        // renderConnections(graphics); // (From Turn 1)
        super.render(graphics, mouseX, mouseY, partialTick); // Draws the nodes

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

        // Draw the Detail Panel if a tech is selected [cite: 355-356]
        if (this.selectedTech != null) {
            renderDetailPanel(graphics);
        }
    }

    private void renderDetailPanel(GuiGraphics graphics) {
        int panelWidth = 160;
        int startX = this.width - panelWidth;

        // Draw Panel Background (Dark transparent box on the right side)
        graphics.fill(startX, 0, this.width, this.height, 0xDD000000);
        graphics.vLine(startX, 0, this.height, 0xFF888888); // Border

        // Get the Tech Data from the Registry
        TechnologyData techData = null;
        for (var entry : HorizonRegistries.TECHNOLOGIES.getEntries()) {
            if (entry.getId().equals(this.selectedTech)) {
                techData = entry.get();
                break;
            }
        }

        if (techData == null) return;

        // Draw Details
        int y = 20;
        graphics.drawString(this.font, Component.literal(this.selectedTech.getPath().toUpperCase()), startX + 10, y, 0xFFDF00);
        y += 20;

        // Description
        graphics.drawWordWrap(this.font, Component.translatable(techData.getDescriptionKey()), startX + 10, y, panelWidth - 20, 0xAAAAAA);
        y += 40;

        // Time Cost
        int seconds = techData.getResearchTimeTicks() / 20;
        graphics.drawString(this.font, Component.translatable("gui.horizoncore.research_time").append(": " + seconds + "s"), startX + 10, y, 0xFFFFFF);
        y += 20;

        // Material Costs (Draw items) [cite: 355-356]
        graphics.drawString(this.font, Component.translatable("gui.horizoncore.requires") + ":", startX + 10, y, 0xFF5555);
        y += 15;
        for (net.minecraft.world.item.ItemStack cost : techData.getResearchCost()) {
            graphics.renderItem(cost, startX + 10, y);
            graphics.renderItemDecorations(this.font, cost, startX + 10, y);
            graphics.drawString(this.font, cost.getHoverName(), startX + 30, y + 4, 0xFFFFFF);
            y += 20;
        }
    }
}