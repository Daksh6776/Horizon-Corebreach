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

    // --- ADDED FOR CLIENT RENDERER ---
    private ResearchProject activeProject;

    public ResearchProject getActiveProject() {
        return this.activeProject;
    }

    public void setActiveProject(ResearchProject project) {
        this.activeProject = project;
    }
    // ---------------------------------

    // The 9 material slots specified in the technical design
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

        // ==========================================
        // HORIZON ANTI-LAG SYSTEM (Throttled Ticking)
        // ==========================================
        // Get the nearest player within our configured simulation radius
        int radius = com.horizonpack.horizoncore.core.HorizonConfig.COMMON.simulationRadius.get();
        net.minecraft.world.entity.player.Player nearestPlayer = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), radius, false);

        // If no player is nearby, we enter "Sleep Mode".
        // In Sleep Mode, we only allow the machine to tick if the current game time is a multiple of 10.
        // This cuts the server load of this machine by 90%!
        if (nearestPlayer == null && level.getGameTime() % 10 != 0) {
            return; // Skip this tick entirely
        }
        // ==========================================

        WorldAgeData worldData = WorldAgeData.get(serverLevel);
        ResearchProject currentProject = worldData.getActiveResearch(pos);

        entity.setActiveProject(currentProject);

        if (currentProject != null) {

            // If we are in Sleep Mode (1/10th tick rate), we need to simulate 10 ticks at once
            // so the research doesn't take 10x longer to finish!
            int ticksToProcess = (nearestPlayer == null) ? 10 : 1;

            currentProject.setSpeedMultiplier(1.0f);

            // Process the tick(s)
            boolean isFinished = false;
            for (int i = 0; i < ticksToProcess; i++) {
                if (currentProject.tick()) {
                    isFinished = true;
                    break;
                }
            }

            if (isFinished) {
                ServerPlayer player = null;

                ResearchCompleteEvent completeEvent = new ResearchCompleteEvent(serverLevel, pos, currentProject, player);
                if (NeoForge.EVENT_BUS.post(completeEvent).isCanceled()) return;

                consumeMaterials(entity.inventory, currentProject.getTechnologyId());

                if (player != null) {
                    IHorizonPlayerData playerData = HorizonCapabilities.get(player);
                    if (playerData != null) {
                        playerData.unlockTechnology(currentProject.getTechnologyId());
                        NeoForge.EVENT_BUS.post(new TechnologyUnlockEvent(player, currentProject.getTechnologyId()));
                        player.displayClientMessage(net.minecraft.network.chat.Component.literal("§aResearch Complete: " + currentProject.getTechnologyId().getPath()), true);
                    }
                }

                worldData.clearActiveResearch(pos);
                entity.setActiveProject(null);
            }
        }
    }

    private static void consumeMaterials(ItemStackHandler inventory, net.minecraft.resources.ResourceLocation techId) {
        // Implementation left standard to NeoForge item handling.
    }
}