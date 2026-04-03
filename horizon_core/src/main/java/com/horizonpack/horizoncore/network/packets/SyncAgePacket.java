package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import io.netty.buffer.ByteBuf;

public record SyncAgePacket(int ageOrdinal) implements CustomPacketPayload {
    public static final Type<SyncAgePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "sync_age"));
    public static final StreamCodec<ByteBuf, SyncAgePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncAgePacket::ageOrdinal,
            SyncAgePacket::new
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    public static void handleClient(SyncAgePacket payload, IPayloadContext ctx) { /* Phase 6 */ }
}