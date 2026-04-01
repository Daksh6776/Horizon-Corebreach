package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class SurvivalTicker {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) { // Once per second
            IHorizonPlayerData data = HorizonCapabilities.get(player);
            if (data == null) return;

            // 💧 HYDRATION DRAIN
            float drainRate = 0.05f;
            if (player.isSprinting()) drainRate = 0.2f;
            if (player.level().getBiome(player.blockPosition()).value().getBaseTemperature() > 0.8f) {
                drainRate *= 2; // Deserts
            }

            data.setHydration(Math.max(0, data.getHydration() - drainRate));

            // 🌡️ TEMPERATURE LOGIC
            float targetTemp = 37.0f; // Ideal body temp
            float ambient = player.level().getBiome(player.blockPosition()).value().getBaseTemperature();

            // Basic math: Ambient 0.5 is neutral. Higher = Heat, Lower = Cold.
            if (ambient > 0.8f) targetTemp += 2.0f; // Hot Biome
            if (ambient < 0.2f) targetTemp -= 3.0f; // Cold Biome
            if (player.isInWater()) targetTemp -= 2.0f; // Water cools you down

            // Slowly drift body temp toward target
            float currentTemp = data.getBodyTemperature();
            if (currentTemp < targetTemp) data.setBodyTemperature(currentTemp + 0.1f);
            if (currentTemp > targetTemp) data.setBodyTemperature(currentTemp - 0.1f);

            // 💀 HARSH DEBUFFS
            if (data.getHydration() <= 0) {
                // At 0 thirst, start taking gradual damage
                if (player.tickCount % 80 == 0) { // Every 4 seconds
                    player.hurt(player.damageSources().starve(), 1.0f);
                }
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 1, false, false, true));
            }

            if (data.getBodyTemperature() > 40.0f) {
                // Heatstroke: Vision blurs and you catch fire
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, true));
                player.setRemainingFireTicks(40);
            }

            // 🦠 DISEASE LOGIC (Swamp Fever) [cite: 8, 318]
            // Safely unwrap the biome registry key to check the name in 1.21.1
            String biomeName = player.level().getBiome(player.blockPosition())
                    .unwrapKey().map(key -> key.location().getPath()).orElse("");

            if (biomeName.contains("swamp")) {
                // Swamps have a passive 0.1% chance per second to cause "Swamp Fever"
                if (player.getRandom().nextFloat() < 0.001f) {
                    applyDisease(player, "swamp_fever");
                }
            }
        }
    }

    /**
     * Helper method to apply disease effects to the player.
     */
    private static void applyDisease(ServerPlayer player, String diseaseId) {
        if (diseaseId.equals("swamp_fever")) {
            // Alert the player
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("§cYou feel a sudden fever coming on..."), true);

            // Apply Nausea and Weakness for 60 seconds (1200 ticks) [cite: 220]
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 1));
        }
    }
}