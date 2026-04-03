package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.data.HorizonAge;
import net.neoforged.fml.common.EventBusSubscriber;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.data.WorldAgeData;
import com.horizonpack.horizoncore.events.custom.AgeAdvancementEvent;
import com.horizonpack.horizoncore.events.custom.DayAdvanceEvent;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import com.horizonpack.horizoncore.network.packets.SyncAgePacket;
import com.horizonpack.horizoncore.network.packets.SyncPlayerDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "horizoncore")
public class HorizonEventHandlers {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        if (player.tickCount % 20 == 0) { // Every second
            IHorizonPlayerData data = HorizonCapabilities.get(player);
            if (data != null) {
                data.tick();
                // Sync HUD-relevant data to client every 5 seconds [cite: 321]
                if (player.tickCount % 100 == 0 && player instanceof ServerPlayer sp) {
                    HorizonPacketHandler.sendToPlayer(new SyncPlayerDataPacket(data.serializeNBT()), sp);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        IHorizonPlayerData original = HorizonCapabilities.get(event.getOriginal());
        IHorizonPlayerData copy = HorizonCapabilities.get(event.getEntity());
        if (original != null && copy != null) copy.copyFrom(original);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            IHorizonPlayerData data = HorizonCapabilities.get(sp);
            if (data != null) {
                HorizonPacketHandler.sendToPlayer(new SyncPlayerDataPacket(data.serializeNBT()), sp);
            }
        }
    }

    @SubscribeEvent
    public static void onTechnologyUnlocked(com.horizonpack.horizoncore.events.custom.TechnologyUnlockEvent event) {
        if (event.getPlayer().level() instanceof ServerLevel level) {
            // Get the technology definition from the registry by iterating the DeferredHolders
            com.horizonpack.horizoncore.data.TechnologyData techEntry = null;
            for (var entry : com.horizonpack.horizoncore.core.HorizonRegistries.TECHNOLOGIES.getEntries()) {
                if (entry.getId().equals(event.getTechnologyId())) {
                    techEntry = entry.get();
                    break;
                }
            }

            if (techEntry != null) {
                // Check all effects on this tech
                for (com.horizonpack.horizoncore.data.TechnologyData.TechEffect effect : techEntry.getEffects()) {
                    if (effect instanceof com.horizonpack.horizoncore.data.TechnologyData.TechEffect.AdvanceAge ageEffect) {
                        // Found an era-advancement effect! Push the global world age forward.
                        WorldAgeData.get(level).tryAdvanceGlobalAge(ageEffect.newAge(), level);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onAgeAdvancement(AgeAdvancementEvent event) {
        if (event.getLevel() instanceof ServerLevel sl) {
            sl.players().forEach(player -> {
                WorldAgeData worldData = WorldAgeData.get(sl);
                // Check if this player's specific location has an override age
                HorizonAge effectiveAge = worldData.getEffectiveAge(player.getUUID(), player.blockPosition());

                HorizonPacketHandler.sendToPlayer(
                        new SyncAgePacket(effectiveAge.ordinal()),
                        (ServerPlayer) player
                );
            });
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(net.neoforged.neoforge.event.level.BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel level) {
            // Get the world age [cite: 260, 289]
            HorizonAge worldAge = WorldAgeData.get(level).getGlobalAge();

            // Check if the block is an ore they can't see yet
            if (!com.horizonpack.horizoncore.data.OreProgression.canPlayerSee(event.getState().getBlock(), worldAge)) {
                event.setCanceled(true); // Stop the break
                // Send a message to the player
                event.getPlayer().displayClientMessage(
                        net.minecraft.network.chat.Component.literal("You don't recognize this mineral yet..."),
                        true
                );
            }
        }
    }
}