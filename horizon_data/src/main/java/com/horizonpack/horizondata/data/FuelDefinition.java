package com.horizonpack.horizondata.data;

import net.minecraft.resources.ResourceLocation;
import java.util.Objects;

/**
 * Represents a fuel source that can be burned in various Horizon workshops.
 */
public class FuelDefinition {
    private final ResourceLocation itemId;
    private final int burnTime;
    private final float heatOutput;
    private final String fuelType;

    /**
     * Constructs a new FuelDefinition.
     *
     * @param itemId     The registry name of the item (e.g., "minecraft:coal")
     * @param burnTime   How long the fuel burns in ticks (20 ticks = 1 second)
     * @param heatOutput The efficiency/heat tier of the fuel (0.0 to 1.0)
     * @param fuelType   The category of the fuel (e.g., "wood", "coal", "nuclear")
     */
    public FuelDefinition(ResourceLocation itemId, int burnTime, float heatOutput, String fuelType) {
        this.itemId = itemId;
        this.burnTime = burnTime;
        this.heatOutput = Math.clamp(heatOutput, 0.0f, 1.0f); // Ensure it stays within bounds
        this.fuelType = fuelType;
    }

    public ResourceLocation getItemId() {
        return itemId;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public float getHeatOutput() {
        return heatOutput;
    }

    public String getFuelType() {
        return fuelType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelDefinition that = (FuelDefinition) o;
        return burnTime == that.burnTime &&
                Float.compare(that.heatOutput, heatOutput) == 0 &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(fuelType, that.fuelType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, burnTime, heatOutput, fuelType);
    }

    @Override
    public String toString() {
        return "FuelDefinition{" +
                "itemId=" + itemId +
                ", burnTime=" + burnTime +
                ", heatOutput=" + heatOutput +
                ", fuelType='" + fuelType + '\'' +
                '}';
    }
}