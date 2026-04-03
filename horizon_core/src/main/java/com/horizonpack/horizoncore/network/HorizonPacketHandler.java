package com.horizonpack.horizoncore.network;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.network.packets.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class HorizonPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(HorizonCore.MODID).versioned(PROTOCOL_VERSION);

        // Server → Client
        reg.playToClient(SyncPlayerDataPacket.TYPE, SyncPlayerDataPacket.STREAM_CODEC, SyncPlayerDataPacket::handleClient);
        reg.playToClient(SyncAgePacket.TYPE, SyncAgePacket.STREAM_CODEC, SyncAgePacket::handleClient);
        reg.playToClient(TechUnlockPacket.TYPE, TechUnlockPacket.STREAM_CODEC, TechUnlockPacket::handleClient);
        reg.playToClient(SkillUpdatePacket.TYPE, SkillUpdatePacket.STREAM_CODEC, SkillUpdatePacket::handleClient);

        // Client → Server
        reg.playToServer(RequestResearchPacket.ID, RequestResearchPacket.STREAM_CODEC, RequestResearchPacket::handleServer);
    }

    public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}