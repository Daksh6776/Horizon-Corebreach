package com.horizonpack.horizoncore.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WorldAgeData extends SavedData {

    private static final String DATA_NAME = "horizon_world_age";

    private int  currentDay  = 0;
    private long currentTick = 0;
    private HorizonAge globalAge = HorizonAge.STONE_AGE;

    private final Map<Long, Integer> settlementDays = new HashMap<>();
    private final Map<UUID, Integer> civilizationDays = new HashMap<>();
    private final Map<Long, ResearchProject> activeResearch = new HashMap<>();

    public WorldAgeData() {}

    public static WorldAgeData get(ServerLevel level) {
        // 1.21.1 requires DataFixTypes.LEVEL here
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(WorldAgeData::new, WorldAgeData::load, DataFixTypes.LEVEL),
                DATA_NAME
        );
    }

    // 1.21.1 requires HolderLookup.Provider
    public static WorldAgeData load(CompoundTag tag, HolderLookup.Provider provider) {
        WorldAgeData data = new WorldAgeData();
        data.currentDay  = tag.getInt("CurrentDay");
        data.currentTick = tag.getLong("CurrentTick");
        data.globalAge   = HorizonAge.values()[tag.getInt("GlobalAge")];

        ListTag research = tag.getList("ActiveResearch", Tag.TAG_COMPOUND);
        for (Tag t : research) {
            CompoundTag r = (CompoundTag) t;
            long pos = r.getLong("Pos");
            ResearchProject project = new ResearchProject();
            project.deserializeNBT(provider, r.getCompound("Project"));
            data.activeResearch.put(pos, project);
        }

        return data;
    }

    // 1.21.1 requires HolderLookup.Provider
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("CurrentDay",  currentDay);
        tag.putLong("CurrentTick", currentTick);
        tag.putInt("GlobalAge",   globalAge.ordinal());

        ListTag research = new ListTag();
        activeResearch.forEach((pos, project) -> {
            CompoundTag r = new CompoundTag();
            r.putLong("Pos", pos);
            r.put("Project", project.serializeNBT(provider));
            research.add(r);
        });
        tag.put("ActiveResearch", research);

        return tag;
    }

    public boolean tryAdvanceGlobalAge(HorizonAge newAge, ServerLevel level) {
        if (newAge.isAtLeast(globalAge) && newAge != globalAge) {
            HorizonAge oldAge = globalAge;
            this.globalAge = newAge;
            setDirty();

            // Fire the API event so other mods (and the HUD) know the era changed
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(
                    new com.horizonpack.horizoncore.events.custom.AgeAdvancementEvent(level, oldAge, newAge, null)
            );
            return true;
        }
        return false;
    }

    public int getCurrentDay() { return currentDay; }
    public long getCurrentTick() { return currentTick; }
    public HorizonAge getGlobalAge() { return globalAge; }

    public @Nullable ResearchProject getActiveResearch(BlockPos pos) { return activeResearch.get(pos.asLong()); }
    public void setActiveResearch(BlockPos pos, ResearchProject project) { activeResearch.put(pos.asLong(), project); setDirty(); }
    public void clearActiveResearch(BlockPos pos) { activeResearch.remove(pos.asLong()); setDirty(); }
}