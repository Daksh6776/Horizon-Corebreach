package com.horizonpack.horizoncore.capabilities;

import com.horizonpack.horizoncore.data.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.*;

public class HorizonPlayerData implements IHorizonPlayerData {

    private float protein = 50.0f, carbohydrates = 50.0f, fats = 50.0f, fiber = 50.0f;
    private float calories = 2000.0f, hydration = 100.0f;
    private float bodyTemperature = 37.0f, exhaustion = 0.0f;
    private boolean enrolledAtUniversity = false;
    private String currentApprenticeship = "";

    private final EnumMap<VitaminType, Float> vitamins = new EnumMap<>(VitaminType.class);
    private final EnumMap<MineralType, Float> minerals = new EnumMap<>(MineralType.class);
    private final EnumMap<SkillType, Integer> skillLevels = new EnumMap<>(SkillType.class);
    private final EnumMap<SkillType, Integer> skillXP = new EnumMap<>(SkillType.class);
    private final Set<ResourceLocation> unlockedTechs = new HashSet<>();
    private final Map<String, Float> languages = new HashMap<>();

    public HorizonPlayerData() {
        for (VitaminType v : VitaminType.values()) vitamins.put(v, 50.0f);
        for (MineralType m : MineralType.values()) minerals.put(m, 50.0f);
        for (SkillType s : SkillType.values()) {
            skillLevels.put(s, 1);
            skillXP.put(s, 0);
        }
    }

    // --- Core logic (abbreviated for the setup phase) ---
    @Override public void tick() { consumeNutrients(0.05f); }
    @Override public void consumeNutrients(float amt) { /* Logic to decay stats */ }
    @Override public void addNutrients(Map<NutrientType, Float> n) { /* Logic to add stats */ }
    @Override public void addSkillXP(SkillType skill, int xp) { /* XP logic */ }
    @Override public void unlockTechnology(ResourceLocation techId) { unlockedTechs.add(techId); }
    @Override public boolean hasCriticalDeficiency() { return protein < 10 || hydration < 10; }
    @Override public void improveLanguage(String lang, float amt) { languages.merge(lang, amt, (o, a) -> Mth.clamp(o+a, 0f, 1f)); }
    @Override public void copyFrom(IHorizonPlayerData other) { this.deserializeNBT(other.serializeNBT()); }

    // --- Getters & Setters ---
    @Override public float getProtein() { return protein; } @Override public void setProtein(float v) { protein = v; }
    @Override public float getCarbohydrates() { return carbohydrates; } @Override public void setCarbohydrates(float v) { carbohydrates = v; }
    @Override public float getFats() { return fats; } @Override public void setFats(float v) { fats = v; }
    @Override public float getFiber() { return fiber; } @Override public void setFiber(float v) { fiber = v; }
    @Override public float getCalories() { return calories; } @Override public void setCalories(float v) { calories = v; }
    @Override public float getHydration() { return hydration; } @Override public void setHydration(float v) { hydration = v; }
    @Override public Map<VitaminType, Float> getVitamins() { return vitamins; }
    @Override public float getVitamin(VitaminType t) { return vitamins.getOrDefault(t, 50f); }
    @Override public void setVitamin(VitaminType t, float v) { vitamins.put(t, v); }
    @Override public Map<MineralType, Float> getMinerals() { return minerals; }
    @Override public float getMineral(MineralType t) { return minerals.getOrDefault(t, 50f); }
    @Override public void setMineral(MineralType t, float v) { minerals.put(t, v); }
    @Override public Map<SkillType, Integer> getSkillLevels() { return skillLevels; }
    @Override public Map<SkillType, Integer> getSkillXP() { return skillXP; }
    @Override public int getSkillLevel(SkillType s) { return skillLevels.getOrDefault(s, 1); }
    @Override public int getSkillXP(SkillType s) { return skillXP.getOrDefault(s, 0); }
    @Override public void setSkillLevel(SkillType s, int l) { skillLevels.put(s, l); }
    @Override public int getEffectiveSkillCap(SkillType s) { return 100; } // Placeholder logic
    @Override public Set<ResourceLocation> getUnlockedTechnologies() { return unlockedTechs; }
    @Override public boolean hasTechnology(ResourceLocation t) { return unlockedTechs.contains(t); }
    @Override public float getBodyTemperature() { return bodyTemperature; } @Override public void setBodyTemperature(float v) { bodyTemperature = v; }
    @Override public float getExhaustion() { return exhaustion; } @Override public void setExhaustion(float v) { exhaustion = v; }
    @Override public boolean isEnrolledAtUniversity() { return enrolledAtUniversity; } @Override public void setEnrolledAtUniversity(boolean v) { enrolledAtUniversity = v; }
    @Override public String getCurrentApprenticeship() { return currentApprenticeship; } @Override public void setCurrentApprenticeship(String v) { currentApprenticeship = v; }
    @Override public Map<String, Float> getLanguageProficiency() { return languages; }
    @Override public float getLanguageProficiency(String lang) { return languages.getOrDefault(lang, 0f); }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Protein", protein);
        tag.putFloat("Carbohydrates", carbohydrates);
        tag.putFloat("Fats", fats);
        tag.putFloat("Fiber", fiber);
        tag.putFloat("Calories", calories);
        tag.putFloat("Hydration", hydration);
        tag.putFloat("BodyTemp", bodyTemperature);
        tag.putFloat("Exhaustion", exhaustion);
        tag.putBoolean("Enrolled", enrolledAtUniversity);
        tag.putString("Apprentice", currentApprenticeship);

        ListTag techList = new ListTag();
        unlockedTechs.forEach(t -> techList.add(StringTag.valueOf(t.toString())));
        tag.put("Techs", techList);

        return tag; // In a full implementation, you would serialize maps here too
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        protein = tag.getFloat("Protein");
        carbohydrates = tag.getFloat("Carbohydrates");
        fats = tag.getFloat("Fats");
        fiber = tag.getFloat("Fiber");
        calories = tag.getFloat("Calories");
        hydration = tag.getFloat("Hydration");
        bodyTemperature = tag.getFloat("BodyTemp");
        exhaustion = tag.getFloat("Exhaustion");
        enrolledAtUniversity = tag.getBoolean("Enrolled");
        currentApprenticeship = tag.getString("Apprentice");

        unlockedTechs.clear();
        ListTag techList = tag.getList("Techs", Tag.TAG_STRING);
        for(Tag t : techList) unlockedTechs.add(new ResourceLocation(t.getAsString()));
    }
}