package com.horizonpack.horizoncore.events.custom;

import com.horizonpack.horizoncore.data.HorizonAge;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;

/** Fired every in-game day [cite: 314] */
public class DayAdvanceEvent extends Event {
    private final ServerLevel level;
    private final int newDay;
    private final HorizonAge currentAge;

    public DayAdvanceEvent(ServerLevel level, int newDay, HorizonAge currentAge) {
        this.level = level;
        this.newDay = newDay;
        this.currentAge = currentAge;
    }

    public ServerLevel getLevel() { return level; }
    public int getNewDay() { return newDay; }
    public HorizonAge getCurrentAge() { return currentAge; }
}