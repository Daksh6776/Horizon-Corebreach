package com.horizonpack.horizoncore.core;

import net.neoforged.neoforge.common.ModConfigSpec;

public class HorizonConfig {

    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    public static final Common COMMON;
    public static final Client CLIENT;

    static {
        var commonPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON      = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        var clientPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT      = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    public static class Common {
        // World
        public final ModConfigSpec.IntValue    continentCount;

        // Progression
        public final ModConfigSpec.IntValue    daysPerAgeMultiplier;
        public final ModConfigSpec.BooleanValue enableAgeRestrictions;
        public final ModConfigSpec.BooleanValue civilizationsCanAdvanceIndependently;

        // Research
        public final ModConfigSpec.DoubleValue researchSpeedMultiplier;
        public final ModConfigSpec.BooleanValue requireMaterialsForResearch;

        // Difficulty
        public final ModConfigSpec.DoubleValue disasterFrequency;
        public final ModConfigSpec.DoubleValue npcAggression;
        public final ModConfigSpec.BooleanValue realisticNutrition;

        // Performance
        public final ModConfigSpec.IntValue simulationRadius;
        public final ModConfigSpec.IntValue maxActiveNPCs;

        Common(ModConfigSpec.Builder b) {
            b.push("world");
            continentCount = b.comment("Number of continents (1–20)")
                    .defineInRange("continentCount", 7, 1, 20);
            b.pop();

            b.push("progression");
            daysPerAgeMultiplier = b.comment("Multiplier applied to all age thresholds")
                    .defineInRange("daysPerAgeMultiplier", 1, 0, 10);
            enableAgeRestrictions = b.comment("Lock recipes and blocks behind age requirements")
                    .define("enableAgeRestrictions", true);
            civilizationsCanAdvanceIndependently = b
                    .comment("Allow NPC civilizations to reach higher ages than the player")
                    .define("civilizationsCanAdvanceIndependently", true);
            b.pop();

            b.push("research");
            researchSpeedMultiplier = b.comment("Global multiplier for all research speeds")
                    .defineInRange("researchSpeedMultiplier", 1.0, 0.1, 10.0);
            requireMaterialsForResearch = b.comment("Research consumes material items when enabled")
                    .define("requireMaterialsForResearch", true);
            b.pop();

            b.push("difficulty");
            disasterFrequency = b.comment("Disaster frequency (0 = none, 1 = maximum)")
                    .defineInRange("disasterFrequency", 0.5, 0.0, 1.0);
            npcAggression     = b.comment("NPC civilization aggression (0–1)")
                    .defineInRange("npcAggression", 0.5, 0.0, 1.0);
            realisticNutrition = b.comment("Enable multi-nutrient food system")
                    .define("realisticNutrition", true);
            b.pop();

            b.push("performance");
            simulationRadius = b.comment("Chunk radius for full simulation (1–32)")
                    .defineInRange("simulationRadius", 8, 1, 32);
            maxActiveNPCs    = b.comment("Maximum simultaneously active NPCs")
                    .defineInRange("maxActiveNPCs", 1000, 100, 10000);
            b.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue showNutritionHUD;
        public final ModConfigSpec.BooleanValue showTemperatureHUD;
        public final ModConfigSpec.BooleanValue showAgeNotification;
        public final ModConfigSpec.BooleanValue showSkillXPToasts;
        public final ModConfigSpec.IntValue     hudScale;

        Client(ModConfigSpec.Builder b) {
            b.push("hud");
            showNutritionHUD    = b.define("showNutritionHUD", true);
            showTemperatureHUD  = b.define("showTemperatureHUD", true);
            showAgeNotification = b.define("showAgeNotification", true);
            showSkillXPToasts   = b.define("showSkillXPToasts", true);
            hudScale            = b.defineInRange("hudScale", 100, 50, 200);
            b.pop();
        }
    }
}