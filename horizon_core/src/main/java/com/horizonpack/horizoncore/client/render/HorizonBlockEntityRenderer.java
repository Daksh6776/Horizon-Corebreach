package com.horizonpack.horizoncore.client.render;

import com.horizonpack.horizoncore.blockentities.ResearchBenchBlockEntity;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HorizonBlockEntityRenderer implements BlockEntityRenderer<ResearchBenchBlockEntity> {

    // 1. Add this variable to cache the project for the client renderer
    private com.horizonpack.horizoncore.data.ResearchProject activeProject;

    // 2. Add this getter method so the Renderer can access it
    public com.horizonpack.horizoncore.data.ResearchProject getActiveProject() {
        return this.activeProject;
    }

    // (Optional but recommended) Add a setter so your Server tick can update it
    public void setActiveProject(com.horizonpack.horizoncore.data.ResearchProject project) {
        this.activeProject = project;
    }
    public HorizonBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Constructor required by NeoForge
    }

    @Override
    public void render(ResearchBenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        // If the bench has an active research project, render a spinning scroll above it
        if (blockEntity.getLevel() != null && blockEntity.getActiveProject() != null) {
            poseStack.pushPose();

            // Move the item to the center of the block and slightly above it
            poseStack.translate(0.5, 1.2, 0.5);

            // Make it spin elegantly based on game time
            long time = blockEntity.getLevel().getGameTime();
            poseStack.mulPose(Axis.YP.rotationDegrees((time + partialTick) * 4));

            // Scale it down slightly so it doesn't look massive
            poseStack.scale(0.75f, 0.75f, 0.75f);

            // Render the Tech Scroll item
            ItemStack displayStack = new ItemStack(HorizonRegistries.TECH_SCROLL.get());
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    displayStack,
                    ItemDisplayContext.GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );

            poseStack.popPose();
        }
    }
}