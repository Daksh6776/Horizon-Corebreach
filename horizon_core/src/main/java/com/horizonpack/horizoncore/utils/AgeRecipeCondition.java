package com.horizonpack.horizoncore.utils;

import com.horizonpack.horizoncore.data.HorizonAge;
import com.horizonpack.horizoncore.data.WorldAgeData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public record AgeRecipeCondition(HorizonAge requiredAge) implements ICondition {

    public static final MapCodec<AgeRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.xmap(s -> HorizonAge.valueOf(s.toUpperCase()), a -> a.name().toLowerCase())
                    .fieldOf("required_age").forGetter(AgeRecipeCondition::requiredAge)
    ).apply(inst, AgeRecipeCondition::new));

    @Override
    public boolean test(IContext context) {
        // Datagen / Client fallback
        if (context == ICondition.IContext.EMPTY) return true;

        // Safely fetch the server instance via NeoForge hooks
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return true;

        ServerLevel overworld = server.overworld();
        if (overworld == null) return true;

        // Compare the global age against the recipe's requirement
        return WorldAgeData.get(overworld).getGlobalAge().isAtLeast(requiredAge);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}