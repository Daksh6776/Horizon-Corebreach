package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.data.TechnologyData;
import com.horizonpack.horizoncore.data.WorldAgeData;
import com.horizonpack.horizoncore.data.HorizonAge;
import com.horizonpack.horizoncore.events.custom.TechnologyUnlockEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class TechnologyEvents {

    @SubscribeEvent
    public static void onTechnologyUnlocked(TechnologyUnlockEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        TechnologyData tech = null;
        for (var entry : HorizonRegistries.TECHNOLOGIES.getEntries()) {
            if (entry.getId().equals(event.getTechnologyId())) {
                tech = entry.get();
                break;
            }
        }

        if (tech == null) return;

        // Note: Check your TechEffect.java class! It MUST have:
        // public String getType() { return type; }
        // We check if any of the effects are specifically the AdvanceAge record!
        boolean triggersAgeUp = tech.getEffects().stream()
                .anyMatch(effect -> effect instanceof TechnologyData.TechEffect.AdvanceAge);

        if (triggersAgeUp) {
            ServerLevel level = player.serverLevel();
            WorldAgeData worldData = WorldAgeData.get(level);

            HorizonAge currentAge = worldData.getCivilizationAge(player.getUUID());
            HorizonAge nextAge = currentAge.getNextAge();

            if (currentAge != nextAge) {
                worldData.setCivilizationAge(player.getUUID(), nextAge);

                player.server.getPlayerList().broadcastSystemMessage(
                        Component.literal("§6[Breakthrough] §e" + player.getName().getString() + " has advanced to the " + nextAge.getDisplayName() + "!"),
                        false
                );
            }
        }
    }
}