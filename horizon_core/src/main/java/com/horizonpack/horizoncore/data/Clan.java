package com.horizonpack.horizoncore.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Clan {
    public final UUID id;
    public String name;
    public UUID leader;
    public int days;
    public final Set<UUID> members = new HashSet<>();

    public Clan(UUID id, String name, UUID leader) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.days = 0;
        this.members.add(leader);
    }

    public HorizonAge getAge() {
        return HorizonAge.fromDay(this.days);
    }
}