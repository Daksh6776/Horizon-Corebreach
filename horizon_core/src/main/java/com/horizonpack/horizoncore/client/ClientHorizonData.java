package com.horizonpack.horizoncore.client;

import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.data.HorizonAge;

public class ClientHorizonData {
    public static IHorizonPlayerData playerData;
    public static int toastTicksRemaining;
    public static String lastSkillToast;
    private static HorizonAge currentAge = HorizonAge.STONE_AGE;
    private static float hydration = 100f;

    public static void setAge(HorizonAge age) { currentAge = age; }
    public static HorizonAge getAge() { return currentAge; }

    public static void setHydration(float h) { hydration = h; }
    public static float getHydration() { return hydration; }
}