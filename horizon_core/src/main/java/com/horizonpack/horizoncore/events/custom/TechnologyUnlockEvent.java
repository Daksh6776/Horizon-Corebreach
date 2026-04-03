package com.horizonpack.horizoncore.events.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

/** Fired when a player unlocks a new technology [cite: 587] */
public class TechnologyUnlockEvent extends Event {
    private final Player player;
    private final ResourceLocation technologyId;

    public TechnologyUnlockEvent(Player player, ResourceLocation technologyId) {
        this.player = player;
        this.technologyId = technologyId;
    }

    public Player getPlayer() { return player; }
    public ResourceLocation getTechnologyId() { return technologyId; }
}