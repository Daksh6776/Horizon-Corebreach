package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TechUnlockPacket(ResourceLocation techId) implements CustomPacketPayload {
    public static final Type<TechUnlockPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "tech_unlock"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TechUnlockPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, TechUnlockPacket::techId,
            TechUnlockPacket::new
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    public static void handleClient(TechUnlockPacket payload, IPayloadContext ctx) { /* Phase 6 */ }
}