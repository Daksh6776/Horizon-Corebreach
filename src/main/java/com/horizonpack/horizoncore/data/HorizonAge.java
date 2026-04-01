package com.horizonpack.horizoncore.data;

import net.minecraft.util.StringRepresentable;

public enum HorizonAge implements StringRepresentable {
    STONE_AGE       ("stone_age",   "Stone Age"),
    COPPER_AGE      ("copper_age",  "Copper Age"),
    BRONZE_AGE      ("bronze_age",  "Bronze Age"),
    IRON_AGE        ("iron_age",    "Iron Age"),
    MEDIEVAL        ("medieval",    "Medieval Era"),
    INDUSTRIAL      ("industrial",  "Industrial Revolution"),
    MODERN          ("modern",      "Modern Era");

    private final String id;
    private final String displayName;

    HorizonAge(String id, String displayName) {
        this.id       = id;
        this.displayName = displayName;
    }

    /** True if this age is at least as advanced as the required age. */
    public boolean isAtLeast(HorizonAge required) {
        return this.ordinal() >= required.ordinal();
    }

    /** True if this age is strictly before the given age. */
    public boolean isBefore(HorizonAge other) {
        return this.ordinal() < other.ordinal();
    }

    public String getDisplayName(){ return displayName; }

    @Override
    public String getSerializedName() { return id; }
}