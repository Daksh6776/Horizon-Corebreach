package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SkillUpdatePacket(String skillName, int level, int xp) implements CustomPacketPayload {
    public static final Type<SkillUpdatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "skill_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SkillUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SkillUpdatePacket::skillName,
            ByteBufCodecs.INT, SkillUpdatePacket::level,
            ByteBufCodecs.INT, SkillUpdatePacket::xp,
            SkillUpdatePacket::new
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    public static void handleClient(SkillUpdatePacket payload, IPayloadContext ctx) { /* Phase 6 */ }
}