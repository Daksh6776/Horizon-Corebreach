package com.horizonpack.horizoncore.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class ResearchProject {
    private ResourceLocation technologyId;
    private int totalTicksRequired;
    private int ticksCompleted;
    private float speedMultiplier = 1.0f;

    public ResearchProject() {}

    public ResearchProject(ResourceLocation technologyId, int totalTicksRequired) {
        this.technologyId       = technologyId;
        this.totalTicksRequired = totalTicksRequired;
    }

    public boolean tick() {
        ticksCompleted += (int)(speedMultiplier);
        return ticksCompleted >= totalTicksRequired;
    }

    public float getProgress()              { return (float) ticksCompleted / totalTicksRequired; }
    public ResourceLocation getTechnologyId(){ return technologyId; }
    public void setSpeedMultiplier(float m) { this.speedMultiplier = m; }
    public float getSpeedMultiplier()       { return speedMultiplier; }

    // Replaced INBTSerializable with standard custom methods requiring HolderLookup.Provider
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (technologyId != null) tag.putString("TechId", technologyId.toString());
        tag.putInt("TotalTicks", totalTicksRequired);
        tag.putInt("TicksDone", ticksCompleted);
        tag.putFloat("SpeedMult", speedMultiplier);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        // In 1.21.1, ResourceLocation parsing uses .parse()
        if (tag.contains("TechId")) technologyId = ResourceLocation.parse(tag.getString("TechId"));
        totalTicksRequired = tag.getInt("TotalTicks");
        ticksCompleted     = tag.getInt("TicksDone");
        speedMultiplier    = tag.getFloat("SpeedMult");
    }
}