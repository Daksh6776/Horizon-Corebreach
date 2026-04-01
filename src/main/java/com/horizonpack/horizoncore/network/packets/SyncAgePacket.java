package com.horizonpack.horizoncore.network.packets;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
public record SyncAgePacket(int ageOrdinal) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
    public static final Type<SyncAgePacket> ID = new Type<>(new ResourceLocation(HorizonCore.MODID, "sync_age"));
    public SyncAgePacket(FriendlyByteBuf buf) { this(buf.readInt()); }
    @Override public void write(FriendlyByteBuf buf) { buf.writeInt(ageOrdinal); }
    @Override public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return ID; }
    public static void handleClient(SyncAgePacket packet, IPayloadContext ctx) { /* Phase 6 */ }
}