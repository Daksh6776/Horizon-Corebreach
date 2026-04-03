package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import com.horizonpack.horizoncore.network.packets.SyncPlayerDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class ThirstEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ServerPlayer player = event.getEntity() instanceof ServerPlayer sp ? sp : null;

        // Only trigger if player is empty-handed and on server
        if (player != null && event.getItemStack().isEmpty()) {
            BlockHitResult hit = (BlockHitResult) player.pick(2.0D, 0.0F, true);

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hit.getBlockPos();
                if (level.getBlockState(pos).is(Blocks.WATER)) {
                    drinkFromSource(player, level, pos);
                }
            }
        }
    }

    private static void drinkFromSource(ServerPlayer player, Level level, BlockPos pos) {
        IHorizonPlayerData data = HorizonCapabilities.get(player);
        if (data == null) return;

        // Determine water quality based on Biome
        boolean isOcean = level.getBiome(pos).value().hasPrecipitation() == false &&
                level.getBiome(pos).getRegisteredName().contains("ocean");

        if (isOcean) {
            // 🌊 SALT WATER: Restores 5% thirst but causes Dehydration (Hunger/Nausea)
            data.setHydration(Math.min(100f, data.getHydration() + 5.0f));
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 1)); // Salt makes you hungrier
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        } else {
            // 💧 DIRTY WATER: Restores 15% thirst but 50% chance of Parasites (Poison/Weakness)
            data.setHydration(Math.min(100f, data.getHydration() + 15.0f));
            if (level.random.nextFloat() < 0.50f) {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 0));
            }
        }

        // Sync to update the GTNH-style HUD
        HorizonPacketHandler.sendToPlayer(new SyncPlayerDataPacket(data.serializeNBT()), player);

        // Play drinking sound
        player.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 0.5f, 1.0f);
    }
}