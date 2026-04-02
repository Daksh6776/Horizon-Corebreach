package com.horizonpack.horizoncore.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TechnologyData {
    private final ResourceLocation id;
    private final HorizonAge requiredAge;
    private final List<ResourceLocation> prerequisites;
    private final int researchTimeTicks;
    private final List<ItemStack> researchCost;
    private final List<TechEffect> effects;
    private final String descriptionKey;
    private final String iconPath;

    public TechnologyData(Builder builder) {
        this.id                 = builder.id;
        this.requiredAge        = builder.requiredAge;
        this.prerequisites      = List.copyOf(builder.prerequisites);
        this.researchTimeTicks  = builder.researchTimeTicks;
        this.researchCost       = List.copyOf(builder.researchCost);
        this.effects            = List.copyOf(builder.effects);
        this.descriptionKey     = builder.descriptionKey;
        this.iconPath           = builder.iconPath;
    }

    public ResourceLocation getId()                     { return id; }
    public HorizonAge getRequiredAge()                  { return requiredAge; }
    public List<ResourceLocation> getPrerequisites()    { return prerequisites; }
    public int getResearchTimeTicks()                   { return researchTimeTicks; }
    public List<ItemStack> getResearchCost()            { return researchCost; }
    public List<TechEffect> getEffects()                { return effects; }
    public String getDescriptionKey()                   { return descriptionKey; }
    public String getIconPath()                         { return iconPath; }

    /** Sealed interface for what a technology unlocks. */
    /** Sealed interface for what a technology unlocks. */
    public sealed interface TechEffect permits
            TechEffect.UnlockRecipeCategory,
            TechEffect.UnlockBlock,
            TechEffect.UnlockSkillCap,
            TechEffect.ModifyAttribute,
            TechEffect.AdvanceAge,          // <-- Added this
            TechEffect.FireEvent {

        record UnlockRecipeCategory(ResourceLocation categoryId) implements TechEffect {}
        record UnlockBlock(ResourceLocation blockId) implements TechEffect {}
        record UnlockSkillCap(SkillType skill, int newMaxLevel) implements TechEffect {}
        record ModifyAttribute(ResourceLocation attributeId, double amount) implements TechEffect {}
        record AdvanceAge(HorizonAge newAge) implements TechEffect {} // <-- Added this
        record FireEvent(String eventKey) implements TechEffect {}
    }

    public static Builder builder(ResourceLocation id) { return new Builder(id); }

    public static final class Builder {
        private final ResourceLocation id;
        private HorizonAge requiredAge = HorizonAge.STONE_AGE;
        private List<ResourceLocation> prerequisites = List.of();
        private int researchTimeTicks = 24000;
        private List<ItemStack> researchCost = List.of();
        private List<TechEffect> effects = List.of();
        private String descriptionKey = "";
        private String iconPath = "";

        private Builder(ResourceLocation id) { this.id = id; }

        public Builder age(HorizonAge age)                        { this.requiredAge = age; return this; }
        public Builder requires(ResourceLocation... prereqs)      { this.prerequisites = List.of(prereqs); return this; }
        public Builder time(int ticks)                            { this.researchTimeTicks = ticks; return this; }
        public Builder cost(List<ItemStack> items)                { this.researchCost = items; return this; }
        public Builder effects(TechEffect... fx)                  { this.effects = List.of(fx); return this; }
        public Builder description(String key)                    { this.descriptionKey = key; return this; }
        public Builder icon(String path)                          { this.iconPath = path; return this; }
        public TechnologyData build()                             { return new TechnologyData(this); }
    }
}