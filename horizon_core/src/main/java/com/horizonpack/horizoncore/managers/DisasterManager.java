package com.horizonpack.horizoncore.managers;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class DisasterManager {
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            // TODO: Implement random disaster events (cave-ins, extreme weather)
        }
    }
}
