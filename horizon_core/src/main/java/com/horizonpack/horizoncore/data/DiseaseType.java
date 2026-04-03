package com.horizonpack.horizoncore.data;

import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;

public record DiseaseType(
        String id,
        String displayName,
        float contractionChance,
        List<MobEffectInstance> effects,
        int durationTicks
) {}