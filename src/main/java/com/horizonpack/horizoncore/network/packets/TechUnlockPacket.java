package com.horizonpack.horizoncore.network.packets;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
public record TechUnlockPacket(ResourceLocation techId) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
    public static final Type<TechUnlockPacket> ID = new Type<>(new ResourceLocation(HorizonCore.MODID, "tech_unlock"));
    public TechUnlockPacket(FriendlyByteBuf buf) { this(buf.readResourceLocation()); }
    @Override public void write(FriendlyByteBuf buf) { buf.writeResourceLocation(techId); }
    @Override public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return ID; }
    public static void handleClient(TechUnlockPacket packet, IPayloadContext ctx) { /* Phase 6 */ }
}