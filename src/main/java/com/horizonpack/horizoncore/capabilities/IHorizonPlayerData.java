package com.horizonpack.horizoncore.capabilities;

import com.horizonpack.horizoncore.data.*;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import java.util.Set;

public interface IHorizonPlayerData {
    // ═══════════════════════════════ NUTRITION ═══════════════════════════════
    float getProtein();            void setProtein(float value);
    float getCarbohydrates();      void setCarbohydrates(float value);
    float getFats();               void setFats(float value);
    float getFiber();              void setFiber(float value);
    float getCalories();           void setCalories(float value);
    float getHydration();          void setHydration(float value);

    Map<VitaminType, Float> getVitamins();
    float getVitamin(VitaminType type);
    void  setVitamin(VitaminType type, float value);

    Map<MineralType, Float> getMinerals();
    float getMineral(MineralType type);
    void  setMineral(MineralType type, float value);

    void addNutrients(Map<NutrientType, Float> nutrients);
    void consumeNutrients(float deltaTime);

    // ═══════════════════════════════ SKILLS ══════════════════════════════════
    Map<SkillType, Integer> getSkillLevels();
    Map<SkillType, Integer> getSkillXP();
    int  getSkillLevel(SkillType skill);
    int  getSkillXP(SkillType skill);
    void addSkillXP(SkillType skill, int xp);
    void setSkillLevel(SkillType skill, int level);
    int  getEffectiveSkillCap(SkillType skill);

    // ═══════════════════════════════ TECHNOLOGY ══════════════════════════════
    Set<ResourceLocation> getUnlockedTechnologies();
    boolean hasTechnology(ResourceLocation techId);
    void unlockTechnology(ResourceLocation techId);

    // ═══════════════════════════════ STATUS ══════════════════════════════════
    float getBodyTemperature();                  void setBodyTemperature(float celsius);
    float getExhaustion();                       void setExhaustion(float value);
    boolean isEnrolledAtUniversity();            void setEnrolledAtUniversity(boolean enrolled);
    String  getCurrentApprenticeship();          void setCurrentApprenticeship(String npcId);

    // ═══════════════════════════════ LANGUAGE ════════════════════════════════
    Map<String, Float> getLanguageProficiency();
    float getLanguageProficiency(String languageId);
    void  improveLanguage(String languageId, float delta);

    // ═══════════════════════════════ UTILITY ═════════════════════════════════
    void tick();
    void copyFrom(IHorizonPlayerData other);
    boolean hasCriticalDeficiency();
}