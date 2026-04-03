package com.horizonpack.horizondata.core;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DataConfig {

    public static final ModConfigSpec COMMON_SPEC;
    public static final DataConfig COMMON;

    // Static block to initialize the builder and specs
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        COMMON = new DataConfig(builder);
        COMMON_SPEC = builder.build();
    }

    // ── Ore Generation ────────────────────────────────────────────────────────
    public final ModConfigSpec.BooleanValue enableCustomOres;
    public final ModConfigSpec.DoubleValue oreFrequencyMultiplier;
    public final ModConfigSpec.BooleanValue enableOilAndGas;
    public final ModConfigSpec.BooleanValue enableRareEarths;

    // ── Structural Integrity ─────────────────────────────────────────────────
    public final ModConfigSpec.BooleanValue enableStructuralIntegrity;
    public final ModConfigSpec.IntValue collapseCheckInterval;
    public final ModConfigSpec.DoubleValue loadMultiplier;

    // Private constructor to build the config categories
    private DataConfig(ModConfigSpec.Builder builder) {

        // Ores Category
        builder.push("ores");
        enableCustomOres = builder
                .comment("Enable HorizonData ore generation (disable if using another ore mod)")
                .define("enable_custom_ores", true);
        oreFrequencyMultiplier = builder
                .comment("Multiplier for ore vein frequency. 1.0 = default, 0.5 = half, 2.0 = double")
                .defineInRange("ore_frequency_multiplier", 1.0, 0.1, 5.0);
        enableOilAndGas = builder
                .comment("Enable oil and natural gas deposit generation (Industrial Age fuels)")
                .define("enable_oil_and_gas", true);
        enableRareEarths = builder
                .comment("Enable rare earth element deposits (required for Modern Age electronics)")
                .define("enable_rare_earths", true);
        builder.pop();

        // Structural Integrity Category
        builder.push("structural_integrity");
        enableStructuralIntegrity = builder
                .comment("If true, buildings can collapse if load-bearing blocks are removed")
                .define("enable_structural_integrity", true);
        collapseCheckInterval = builder
                .comment("Server ticks between integrity checks (default 200 = ~10 seconds)")
                .defineInRange("collapse_check_interval", 200, 20, 12000);
        loadMultiplier = builder
                .comment("Multiplier on all block load values. Increase to make collapses less likely")
                .defineInRange("load_multiplier", 1.0, 0.1, 10.0);
        builder.pop();

        // Note: You can continue using builder.push() and builder.pop() to add
        // the "fuels", "weathering", "workshops", and "block_shapes" categories.
    }
}