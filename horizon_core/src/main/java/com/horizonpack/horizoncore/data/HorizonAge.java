package com.horizonpack.horizoncore.data;

public enum HorizonAge {
    STONE_AGE(0, "Stone Age"),
    COPPER_AGE(50, "Copper Age"),
    BRONZE_AGE(200, "Bronze Age"),
    IRON_AGE(500, "Iron Age"),
    INDUSTRIAL(1500, "Industrial Era"),
    MODERN_AGE(4000, "Modern Era");

    private final int days;
    private final String name;

    HorizonAge(int days, String name) {
        this.days = days;
        this.name = name;
    }

    public int getRequiredDays() { return days; }
    public String getDisplayName() { return name; }

    public HorizonAge getNextAge() {
        int next = this.ordinal() + 1;
        return next < values().length ? values()[next] : this;
    }

    /**
     * Checks if this age is equal to or further advanced than another.
     */
    public boolean isAtLeast(HorizonAge other) {
        return this.ordinal() >= other.ordinal();
    }

    public static HorizonAge fromDay(int day) {
        HorizonAge highest = STONE_AGE;
        for (HorizonAge age : values()) {
            if (day >= age.days) highest = age;
            else break;
        }
        return highest;
    }
}