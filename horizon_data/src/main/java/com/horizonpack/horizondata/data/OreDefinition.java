package com.horizonpack.horizondata.data;

import com.horizonpack.horizoncore.data.HorizonAge;

public record OreDefinition(
        String name,        // This automatically creates def.name()
        int veinSize,
        int count,
        int minY,
        int maxY,
        HorizonAge age
) {}