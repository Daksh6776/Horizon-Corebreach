package com.horizonpack.horizoncore.events.custom;

import com.horizonpack.horizoncore.data.SkillType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

/** Fired when a player's skill level increases [cite: 310] */
public class SkillLevelUpEvent extends Event {
    private final Player player;
    private final SkillType skill;
    private final int oldLevel;
    private final int newLevel;

    public SkillLevelUpEvent(Player player, SkillType skill, int oldLevel, int newLevel) {
        this.player = player;
        this.skill = skill;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Player getPlayer() { return player; }
    public SkillType getSkill() { return skill; }
    public int getOldLevel() { return oldLevel; }
    public int getNewLevel() { return newLevel; }
}