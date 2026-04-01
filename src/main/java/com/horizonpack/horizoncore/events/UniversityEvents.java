package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class UniversityEvents {

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;

        BlockPos clickedPos = event.getPos();
        BlockState clickedState = event.getLevel().getBlockState(clickedPos);

        // Check if the player is clicking a Lectern
        if (clickedState.is(Blocks.LECTERN)) {

            // Assume the Lectern is placed on the floor of the University (Y=1)
            // Therefore, the center bottom of the structure (the floor) is 1 block below it.
            BlockPos centerFloorPos = clickedPos.below();

            if (MultiblockValidator.isUniversityValid(event.getLevel(), centerFloorPos)) {

                // Get the player's custom data
                IHorizonPlayerData data = HorizonCapabilities.get(event.getEntity());
                if (data != null && !data.isEnrolledAtUniversity()) {

                    // Enroll the player!
                    data.setEnrolledAtUniversity(true);
                    event.getEntity().displayClientMessage(
                            Component.literal("§aSuccessfully enrolled at the University! Academic XP is now boosted."),
                            true
                    );

                    // Optional: Play a sound effect or spawn particles
                    event.getLevel().playSound(null, clickedPos, net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);

                    event.setCanceled(true); // Stop default Lectern behavior (like opening a book) if we enrolled them
                }
            } else {
                // Optional debug message to tell them the structure is broken
                // event.getEntity().displayClientMessage(Component.literal("§cThis Lectern is not inside a valid University structure."), true);
            }
        }
    }
}