package com.horizonpack.horizoncore.capabilities;

import com.horizonpack.horizoncore.core.HorizonCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class HorizonCapabilities {

    public static final EntityCapability<IHorizonPlayerData, Void> PLAYER_DATA =
            EntityCapability.create(
                    new ResourceLocation(HorizonCore.MODID, "player_data"),
                    IHorizonPlayerData.class,
                    Void.class
            );

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                PLAYER_DATA,
                net.minecraft.world.entity.EntityType.PLAYER,
                (player, context) -> new HorizonPlayerData()
        );
    }

    /** Convenience method for safely fetching capability data */
    public static @Nullable IHorizonPlayerData get(Player player) {
        return player.getCapability(PLAYER_DATA);
    }
}