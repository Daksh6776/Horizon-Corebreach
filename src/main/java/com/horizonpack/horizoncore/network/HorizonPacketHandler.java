package com.horizonpack.horizoncore.network;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.network.packets.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class HorizonPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(HorizonCore.MODID).versioned(PROTOCOL_VERSION);

        // Server → Client
        reg.play(SyncPlayerDataPacket.ID, SyncPlayerDataPacket::new, h -> h.client(SyncPlayerDataPacket::handleClient));
        reg.play(SyncAgePacket.ID, SyncAgePacket::new, h -> h.client(SyncAgePacket::handleClient));
        reg.play(TechUnlockPacket.ID, TechUnlockPacket::new, h -> h.client(TechUnlockPacket::handleClient));
        reg.play(SkillUpdatePacket.ID, SkillUpdatePacket::new, h -> h.client(SkillUpdatePacket::handleClient));

        // Client → Server
        reg.play(RequestResearchPacket.ID, RequestResearchPacket::new, h -> h.server(RequestResearchPacket::handleServer));
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(Object packet) {
        PacketDistributor.sendToServer(packet);
    }
}