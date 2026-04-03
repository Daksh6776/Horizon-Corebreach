package com.horizonpack.horizoncore.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.util.datafix.DataFixTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class WorldAgeData extends SavedData {
    private int globalDay = 0;
    private long globalTick = 0;
    private final Map<UUID, Integer> playerDays = new HashMap<>();
    private final Map<UUID, ClanData> clans = new HashMap<>();
    private final Map<UUID, UUID> playerToClan = new HashMap<>();
    private final Map<Long, Integer> settlementDays = new HashMap<>();
    private final Map<Long, ResearchProject> activeResearch = new HashMap<>(); // Added back for Research Bench

    public WorldAgeData() {}

    public static WorldAgeData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(WorldAgeData::new, WorldAgeData::load, DataFixTypes.LEVEL),
                "horizon_world_age"
        );
    }

    public void tick(ServerLevel level) {
        this.globalTick++;
        if (this.globalTick % 24000L == 0) {
            this.globalDay++;
            setDirty();
        }
    }

    public void tryAdvanceGlobalAge(HorizonAge newAge, ServerLevel level) {
        if (newAge.ordinal() > getGlobalAge().ordinal()) {
            this.globalDay = newAge.getRequiredDays();
            setDirty();
        }
    }

    public HorizonAge getEffectiveAge(UUID playerUUID, @Nullable BlockPos pos) {
        int personal = playerDays.getOrDefault(playerUUID, 0);
        int clanVal = 0;
        if (playerToClan.containsKey(playerUUID)) {
            UUID clanId = playerToClan.get(playerUUID);
            if (clans.containsKey(clanId)) clanVal = clans.get(clanId).days;
        }
        int settleVal = (pos != null) ? settlementDays.getOrDefault(pos.asLong(), 0) : 0;
        return HorizonAge.fromDay(Math.max(personal, Math.max(clanVal, settleVal)));
    }

    public HorizonAge getCivilizationAge(UUID playerUUID) {
        return getEffectiveAge(playerUUID, null);
    }

    public void setCivilizationAge(UUID playerUUID, HorizonAge age) {
        playerDays.put(playerUUID, age.getRequiredDays());
        setDirty();
    }

    public void setDay(int day, ServerLevel level) {
        this.globalDay = day;
        this.globalTick = (long) day * 24000L;
        level.setDayTime(this.globalTick);
        setDirty();
    }

    public void setday(int day) {
        this.globalDay = day;
        setDirty();
    }

    public HorizonAge getGlobalAge() { return HorizonAge.fromDay(globalDay); }

    // Research Bench Methods added back
    public @Nullable ResearchProject getActiveResearch(BlockPos pos) { return activeResearch.get(pos.asLong()); }
    public void setActiveResearch(BlockPos pos, ResearchProject project) { activeResearch.put(pos.asLong(), project); setDirty(); }
    public void clearActiveResearch(BlockPos pos) { activeResearch.remove(pos.asLong()); setDirty(); }

    public @Nullable UUID getClanIdFromLeader(UUID leaderId) {
        for (var entry : clans.entrySet()) {
            if (entry.getValue().leader.equals(leaderId)) return entry.getKey();
        }
        return null;
    }

    public void createClan(UUID leader, String name) {
        UUID id = UUID.randomUUID();
        clans.put(id, new ClanData(name, leader));
        playerToClan.put(leader, id);
        setDirty();
    }

    public void joinClan(UUID player, UUID clanId) {
        playerToClan.put(player, clanId);
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider p) {
        tag.putInt("GlobalDay", globalDay);
        return tag;
    }

    public static WorldAgeData load(CompoundTag tag, HolderLookup.Provider p) {
        WorldAgeData data = new WorldAgeData();
        data.globalDay = tag.getInt("GlobalDay");
        return data;
    }

    public static class ClanData {
        public String name; public UUID leader; public int days;
        public ClanData(String n, UUID l) { this.name = n; this.leader = l; }
    }
}