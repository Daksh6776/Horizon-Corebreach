package com.horizonpack.horizoncore.events.custom;

import com.horizonpack.horizoncore.data.HorizonAge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.Nullable;

/** Fired when an age advancement occurs (global or settlement-specific) [cite: 580] */
public class AgeAdvancementEvent extends Event implements ICancellableEvent {
    private final Level level;
    private final HorizonAge oldAge;
    private final HorizonAge newAge;
    private final @Nullable BlockPos settlementPos;

    public AgeAdvancementEvent(Level level, HorizonAge oldAge, HorizonAge newAge, @Nullable BlockPos settlementPos) {
        this.level = level;
        this.oldAge = oldAge;
        this.newAge = newAge;
        this.settlementPos = settlementPos;
    }

    public Level getLevel() { return level; }
    public HorizonAge getOldAge() { return oldAge; }
    public HorizonAge getNewAge() { return newAge; }
    public @Nullable BlockPos getSettlementPos() { return settlementPos; }
    public boolean isGlobalAdvancement() { return settlementPos == null; }
}