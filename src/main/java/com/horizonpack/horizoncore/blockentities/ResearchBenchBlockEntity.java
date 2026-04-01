package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.data.ResearchProject;
import com.horizonpack.horizoncore.data.WorldAgeData;
import com.horizonpack.horizoncore.events.custom.ResearchCompleteEvent;
import com.horizonpack.horizoncore.events.custom.TechnologyUnlockEvent;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ResearchBenchBlockEntity extends BlockEntity {

    // The 9 material slots specified in the technical design [cite: 213]
    public final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ResearchBenchBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.RESEARCH_BENCH_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ResearchBenchBlockEntity entity) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;

        WorldAgeData worldData = WorldAgeData.get(serverLevel);
        ResearchProject activeProject = worldData.getActiveResearch(pos);

        if (activeProject != null) {
            // Calculate speed modifiers (Libraries/Universities) here later [cite: 194-195, 206-212]
            activeProject.setSpeedMultiplier(1.0f);

            if (activeProject.tick()) {
                // 1. Research is Complete! Find the player who started it.
                ServerPlayer player = (ServerPlayer) serverLevel.getPlayerByUUID(activeProject.getInitiatorId());

                // 2. Fire the cancellable completion event
                ResearchCompleteEvent completeEvent = new ResearchCompleteEvent(serverLevel, pos, activeProject, player);
                if (NeoForge.EVENT_BUS.post(completeEvent).isCanceled()) return;

                // 3. Consume the required materials (Assuming you have a helper method to drain items)
                consumeMaterials(entity.inventory, activeProject.getTechnologyId());

                // 4. Unlock the Technology for the Player [cite: 235-236, 308-309]
                if (player != null) {
                    IHorizonPlayerData playerData = HorizonCapabilities.get(player);
                    if (playerData != null) {
                        playerData.unlockTechnology(activeProject.getTechnologyId());
                        // Fire the unlock event to trigger age advancements or recipe unlocks
                        NeoForge.EVENT_BUS.post(new TechnologyUnlockEvent(player, activeProject.getTechnologyId()));
                        player.displayClientMessage(net.minecraft.network.chat.Component.literal("§aResearch Complete: " + activeProject.getTechnologyId().getPath()), true);
                    }
                }

                // 5. Clear the active project from the world data [cite: 295-296]
                worldData.clearActiveResearch(pos);
            }
        }
    }

    private static void consumeMaterials(ItemStackHandler inventory, net.minecraft.resources.ResourceLocation techId) {
        // Look up the TechnologyData from HorizonRegistries.TECHNOLOGIES
        // Iterate through tech.getResearchCost() and extract those amounts from the ItemStackHandler [cite: 112, 125, 213]
        // Implementation left standard to NeoForge item handling.
    }
}