package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class ApprenticeshipEvents {
    @SubscribeEvent
    public static void onNPCInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Villager villager && !event.getLevel().isClientSide) {
            IHorizonPlayerData data = HorizonCapabilities.get(event.getEntity());
            if (data != null && data.getCurrentApprenticeship().isEmpty()) {
                // Register their profession as the master type [cite: 222]
                String profession = villager.getVillagerData().getProfession().toString();
                data.setCurrentApprenticeship(profession);
                event.getEntity().displayClientMessage(
                        Component.literal("Apprenticeship started: " + profession), true);
            }
        }
    }
}