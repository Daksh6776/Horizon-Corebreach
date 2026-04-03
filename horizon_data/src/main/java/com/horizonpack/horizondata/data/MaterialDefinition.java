package com.horizonpack.horizondata.data;

import java.util.Objects;

/**
 * Defines the physical properties of custom materials used in HorizonCore.
 */
public class MaterialDefinition {
    private final String materialId;
    private final float hardness;
    private final float blastResistance;
    private final float conductivity;
    private final float density;
    private final boolean isMetal;
    private final boolean isConductor;
    private final int meltingPoint;

    /**
     * Constructs a new physical material definition.
     */
    public MaterialDefinition(String materialId, float hardness, float blastResistance, float conductivity,
                              float density, boolean isMetal, boolean isConductor, int meltingPoint) {
        this.materialId = materialId;
        this.hardness = hardness;
        this.blastResistance = blastResistance;
        this.conductivity = conductivity;
        this.density = density;
        this.isMetal = isMetal;
        this.isConductor = isConductor;
        this.meltingPoint = meltingPoint;
    }

    public String getMaterialId() { return materialId; }
    public float getHardness() { return hardness; }
    public float getBlastResistance() { return blastResistance; }
    public float getConductivity() { return conductivity; }
    public float getDensity() { return density; }
    public boolean isMetal() { return isMetal; }
    public boolean isConductor() { return isConductor; }
    public int getMeltingPoint() { return meltingPoint; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialDefinition that = (MaterialDefinition) o;
        return Float.compare(that.hardness, hardness) == 0 &&
                Float.compare(that.blastResistance, blastResistance) == 0 &&
                Float.compare(that.conductivity, conductivity) == 0 &&
                Float.compare(that.density, density) == 0 &&
                isMetal == that.isMetal &&
                isConductor == that.isConductor &&
                meltingPoint == that.meltingPoint &&
                Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, hardness, blastResistance, conductivity, density, isMetal, isConductor, meltingPoint);
    }

    @Override
    public String toString() {
        return "MaterialDefinition{" +
                "materialId='" + materialId + '\'' +
                ", hardness=" + hardness +
                ", blastResistance=" + blastResistance +
                ", conductivity=" + conductivity +
                ", density=" + density +
                ", isMetal=" + isMetal +
                ", isConductor=" + isConductor +
                ", meltingPoint=" + meltingPoint +
                '}';
    }
}