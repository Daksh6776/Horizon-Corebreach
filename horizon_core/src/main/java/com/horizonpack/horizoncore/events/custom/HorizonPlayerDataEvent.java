package com.horizonpack.horizoncore.events.custom;

import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

/**
 * Fired on the NeoForge EVENT_BUS whenever a significant change occurs
 * to a player's HorizonPlayerData (e.g., massive nutrition drop, temperature spike).
 */
public class HorizonPlayerDataEvent extends Event {
    private final Player player;
    private final IHorizonPlayerData data;

    public HorizonPlayerDataEvent(Player player, IHorizonPlayerData data) {
        this.player = player;
        this.data = data;
    }

    public Player getPlayer() { return player; }
    public IHorizonPlayerData getData() { return data; }
}