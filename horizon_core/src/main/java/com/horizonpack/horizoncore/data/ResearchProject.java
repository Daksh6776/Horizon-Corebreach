package com.horizonpack.horizoncore.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.HolderLookup;
import java.util.UUID;

public class ResearchProject {
    private ResourceLocation technologyId;
    private int totalTicksRequired;
    private int ticksCompleted;
    private float speedMultiplier = 1.0f;
    private UUID initiatorId; // NEW: Tracks who started it

    public ResearchProject() {}

    public ResearchProject(ResourceLocation technologyId, int totalTicksRequired, UUID initiatorId) {
        this.technologyId = technologyId;
        this.totalTicksRequired = totalTicksRequired;
        this.initiatorId = initiatorId;
    }

    public boolean tick() {
        ticksCompleted += Math.max(1, (int) speedMultiplier);
        return ticksCompleted >= totalTicksRequired;
    }

    public float getProgress() { return (float) ticksCompleted / totalTicksRequired; }
    public ResourceLocation getTechnologyId() { return technologyId; }
    public void setSpeedMultiplier(float m) { this.speedMultiplier = m; }
    public float getSpeedMultiplier() { return speedMultiplier; }
    public UUID getInitiatorId() { return initiatorId; }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("TechId", technologyId.toString());
        tag.putInt("TotalTicks", totalTicksRequired);
        tag.putInt("TicksDone", ticksCompleted);
        tag.putFloat("SpeedMult", speedMultiplier);
        if (initiatorId != null) tag.putUUID("Initiator", initiatorId);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        technologyId = ResourceLocation.parse(tag.getString("TechId"));
        totalTicksRequired = tag.getInt("TotalTicks");
        ticksCompleted = tag.getInt("TicksDone");
        speedMultiplier = tag.getFloat("SpeedMult");
        if (tag.hasUUID("Initiator")) initiatorId = tag.getUUID("Initiator");
    }
}