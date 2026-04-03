package com.horizonpack.horizoncore.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

public class HorizonNbt {

    public static <K extends Enum<K>> ListTag serializeEnumIntMap(Map<K, Integer> map) {
        ListTag list = new ListTag();
        map.forEach((key, value) -> {
            CompoundTag entry = new CompoundTag();
            entry.putString("K", key.name());
            entry.putInt("V", value);
            list.add(entry);
        });
        return list;
    }

    public static <K extends Enum<K>> Map<K, Integer> deserializeEnumIntMap(ListTag list, Class<K> enumClass) {
        Map<K, Integer> map = new HashMap<>();
        for (Tag tag : list) {
            CompoundTag entry = (CompoundTag) tag;
            try {
                K key = Enum.valueOf(enumClass, entry.getString("K"));
                map.put(key, entry.getInt("V"));
            } catch (IllegalArgumentException ignored) {
                // Skips old enums if you delete a skill/stat later
            }
        }
        return map;
    }
}