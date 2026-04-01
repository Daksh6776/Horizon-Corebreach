package com.horizonpack.horizoncore.client;

import com.horizonpack.horizoncore.capabilities.HorizonPlayerData;
import com.horizonpack.horizoncore.data.HorizonAge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientHorizonData {
    // Stores the client's localized understanding of the world age
    public static HorizonAge currentAge = HorizonAge.STONE_AGE;

    // Stores the client's localized player data
    public static final HorizonPlayerData playerData = new HorizonPlayerData();

    // Skill toast queue (populated by SkillUpdatePacket)
    public static String lastSkillToast = "";
    public static int toastTicksRemaining = 0;

    public static void setSkillToast(String skillName, int xp) {
        lastSkillToast = "+" + xp + " " + skillName + " XP";
        toastTicksRemaining = 60; // 3 seconds at 20 ticks
    }
}