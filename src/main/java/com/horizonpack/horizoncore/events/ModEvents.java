package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.voice.HorizonVoicePlugin;
import com.horizonpack.horizoncore.commands.AdminCommands;
import com.horizonpack.horizoncore.commands.ClanCommands;
import com.horizonpack.horizoncore.data.HorizonAge;
import com.horizonpack.horizoncore.data.OreProgression;
import com.horizonpack.horizoncore.data.WorldAgeData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "horizoncore") // Make sure "horizoncore" matches your actual Mod ID
public class ModEvents {

    /**
     * Ticks the world age data and triggers ore evolution.
     */
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            // We only track the age and run evolution in the Overworld
            if (serverLevel.dimension() == Level.OVERWORLD) {
                WorldAgeData.get(serverLevel).tick(serverLevel);
            }
        }
    }

    /**
     * Prevents players from mining ores they haven't "unlocked" via their Age.
     */


    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            WorldAgeData data = WorldAgeData.get(level);

            // Get effective age based on Player AND Location
            HorizonAge age = data.getEffectiveAge(event.getPlayer().getUUID(), event.getPos());

            if (!OreProgression.canPlayerMine(event.getState().getBlock(), age)) {
                event.setCanceled(true);
                event.getPlayer().displayClientMessage(
                        Component.literal("§cYour current knowledge (Age: " + age.name() + ") is insufficient!"),
                        true
                );
            }
        }
    }

    // Logic for a command: /horizon setday <number>
    public static void setWorldDay(ServerLevel level, int newDay) {
        WorldAgeData data = WorldAgeData.get(level);
        // Directly setting the day will trigger the Age update in the next tick()
        // You'd need to add a setter for currentDay in WorldAgeData
        data.setday(newDay);
        level.setDayTime(newDay * 24000L);
    }

    @SubscribeEvent


    // CONCEPT: Hooking into Simple Voice Chat API
    // You would use the VoicechatServerApi to set the distance
    public static void updateVoiceDistance(ServerPlayer player, HorizonAge age) {
        double distance = switch (age) {
            case STONE_AGE -> 10.0;
            case COPPER_AGE -> 25.0;
            case IRON_AGE -> 60.0;
            default -> 128.0; // Modern/Industrial
        };
        // Call SVC API here to set player's voice distance
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        ClanCommands.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onChatMessage(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        WorldAgeData data = WorldAgeData.get(player.serverLevel());
        HorizonAge age = data.getEffectiveAge(player.getUUID(), player.blockPosition());

        // Era-Locked Text Chat: Stone Age cannot shout globally
        if (!age.isAtLeast(HorizonAge.IRON_AGE)) {
            event.setCanceled(true);
            String localMsg = "§7[Local] " + player.getName().getString() + ": " + event.getRawText();

            player.serverLevel().players().stream()
                    .filter(p -> p.distanceToSqr(player) < 1600) // 40 block range
                    .forEach(p -> p.sendSystemMessage(Component.literal(localMsg)));
        }
    }

    // Inside ModEvents.java

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        // Register both your Clan commands and Admin commands
        ClanCommands.register(event.getDispatcher());
        AdminCommands.register(event.getDispatcher());


    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // We only check every 5 seconds (100 ticks) to save performance
            if (player.tickCount % 100 == 0) {
                WorldAgeData data = WorldAgeData.get(player.serverLevel());
                HorizonAge age = data.getEffectiveAge(player.getUUID(), player.blockPosition());

                // --- THE API INTEGRATION ---
                // This calls the Voice Plugin we defined earlier
                HorizonVoicePlugin.updatePlayerVoice(player, age);

                // SPECIAL: If they are in the -10,000 deep zone,
                // even if they are in the Modern Age, the "Pressure" reduces voice range.
                if (player.getY() < -5000) {
                    // Logic to apply a "muffled" effect via SVC API
                }
            }
        }
    }
}



    
