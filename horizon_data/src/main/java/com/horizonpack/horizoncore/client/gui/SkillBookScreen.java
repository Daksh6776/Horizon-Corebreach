package com.horizonpack.horizoncore.client.gui;

import com.horizonpack.horizoncore.client.ClientHorizonData;
import com.horizonpack.horizoncore.data.SkillType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillBookScreen extends Screen {

    // Domains defined in the technical specification
    private final List<String> domains = List.of(
            "survival", "combat", "crafting", "agriculture", "academic", "social", "exploration"
    );
    private String selectedDomain = "survival";

    public SkillBookScreen() {
        // Uses the translation key from your en_us.json [cite: 394]
        super(Component.translatable("gui.horizoncore.skill_book.title"));
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        // 1. Create Category Buttons on the left side of the screen
        int startY = 40;
        for (String domain : domains) {
            Button domainBtn = Button.builder(Component.literal(domain.toUpperCase()), btn -> {
                this.selectedDomain = domain;
                this.init(); // Refresh screen to show new domain
            }).bounds(10, startY, 90, 20).build();

            // Dim the button if it's not the currently selected tab
            domainBtn.active = !domain.equals(this.selectedDomain);
            this.addRenderableWidget(domainBtn);

            startY += 24;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw the dark background
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        // Draw the Title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

        // Draw the vertical separator line
        graphics.vLine(110, 30, this.height - 10, 0xFF888888);

        // Render the actual skills for the selected domain
        renderSkills(graphics);
    }

    private void renderSkills(GuiGraphics graphics) {
        int startX = 120;
        int startY = 40;

        // Failsafe if player data isn't synced yet
        if (ClientHorizonData.playerData == null) {
            graphics.drawString(this.font, "Waiting for Server Data...", startX, startY, 0xFF5555);
            return;
        }

        // Loop through all 34 skills
        for (SkillType skill : SkillType.values()) {
            if (skill.getDomain().equals(this.selectedDomain)) {
                // Fetch the player's levels from the capability [cite: 231-232]
                int level = ClientHorizonData.playerData.getSkillLevel(skill);
                int xp = ClientHorizonData.playerData.getSkillXP(skill);

                // 1. Draw Skill Name
                graphics.drawString(this.font, skill.getDisplayName(), startX, startY, 0xFFDF00);

                // 2. Draw Current Level
                graphics.drawString(this.font, "Level " + level, startX + 150, startY, 0xFFFFFF);

                // 3. Draw XP Progress Text
                graphics.drawString(this.font, xp + " XP", startX + 220, startY, 0xAAAAAA);

                // 4. Draw the XP Progress Bar (Background)
                graphics.fill(startX, startY + 12, startX + 250, startY + 16, 0xFF222222);

                // Calculate bar fill (Assuming base 100 XP per level for the visual placeholder)
                int progressWidth = (int) (((float) (xp % 100) / 100.0f) * 250);
                progressWidth = Math.max(0, Math.min(progressWidth, 250)); // Clamp to bar width

                // Draw the XP Progress Bar (Foreground)
                graphics.fill(startX, startY + 12, startX + progressWidth, startY + 16, 0xFF55FFFF);

                // Move down for the next skill in the list
                startY += 30;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}