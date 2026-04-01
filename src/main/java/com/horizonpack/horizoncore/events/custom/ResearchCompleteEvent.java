package com.horizonpack.horizoncore.events.custom;

import com.horizonpack.horizoncore.data.ResearchProject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.Nullable;

/** Fired when a ResearchBenchBlockEntity completes a project [cite: 312] */
public class ResearchCompleteEvent extends Event implements ICancellableEvent {
    private final ServerLevel level;
    private final BlockPos benchPos;
    private final ResearchProject project;
    private final @Nullable Player researchingPlayer;

    public ResearchCompleteEvent(ServerLevel level, BlockPos benchPos, ResearchProject project, @Nullable Player researchingPlayer) {
        this.level = level;
        this.benchPos = benchPos;
        this.project = project;
        this.researchingPlayer = researchingPlayer;
    }

    public ServerLevel getLevel() { return level; }
    public BlockPos getBenchPos() { return benchPos; }
    public ResearchProject getProject() { return project; }
    public @Nullable Player getResearchingPlayer() { return researchingPlayer; }
}