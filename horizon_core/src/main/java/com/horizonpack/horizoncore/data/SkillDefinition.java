package com.horizonpack.horizoncore.data;

import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Map;

public class SkillDefinition {
    private final SkillType type;
    private final int maxLevel;
    private final int baseXpPerLevel;
    private final Map<Integer, List<ResourceLocation>> levelUpUnlocks;

    public SkillDefinition(SkillType type, int maxLevel, int baseXpPerLevel,
                           Map<Integer, List<ResourceLocation>> levelUpUnlocks) {
        this.type             = type;
        this.maxLevel         = maxLevel;
        this.baseXpPerLevel   = baseXpPerLevel;
        this.levelUpUnlocks   = Map.copyOf(levelUpUnlocks);
    }

    public int xpForLevel(int level) {
        return (int)(baseXpPerLevel * Math.pow(level, 1.5));
    }

    public int totalXpForLevel(int level) {
        int total = 0;
        for (int i = 1; i <= level; i++) total += xpForLevel(i);
        return total;
    }

    public SkillType getType()          { return type; }
    public int getMaxLevel()            { return maxLevel; }
    public int getBaseXpPerLevel()      { return baseXpPerLevel; }
    public Map<Integer, List<ResourceLocation>> getLevelUpUnlocks() { return levelUpUnlocks; }
}