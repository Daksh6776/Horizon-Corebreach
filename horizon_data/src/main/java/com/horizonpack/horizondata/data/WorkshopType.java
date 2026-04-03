package com.horizonpack.horizondata.data;

/**
 * Defines all the available workshop types and their physical inventory requirements.
 */
public enum WorkshopType {
    FORGE("forge", 1, 1, true),
    BLOOMERY("bloomery", 1, 1, true),
    CRUCIBLE("crucible", 2, 1, true),
    GRINDSTONE("grindstone", 1, 1, false),
    ANVIL("anvil", 1, 1, false),
    CARPENTRY_BENCH("carpentry_bench", 9, 1, false),
    ALCHEMY_LAB("alchemy_lab", 3, 1, true),
    KITCHEN("kitchen", 4, 1, false),
    LOOM("loom", 2, 1, false),
    POTTERY_WHEEL("pottery_wheel", 1, 1, false),
    TANNERY("tannery", 2, 1, false),
    WORKBENCH("workbench", 9, 1, false);

    private final String workshopId;
    private final int inputSlots;
    private final int outputSlots;
    private final boolean requiresFuel;

    WorkshopType(String workshopId, int inputSlots, int outputSlots, boolean requiresFuel) {
        this.workshopId = workshopId;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.requiresFuel = requiresFuel;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public int getInputSlots() {
        return inputSlots;
    }

    public int getOutputSlots() {
        return outputSlots;
    }

    public boolean requiresFuel() {
        return requiresFuel;
    }

    public String id() {
        return this.workshopId;
    }
}