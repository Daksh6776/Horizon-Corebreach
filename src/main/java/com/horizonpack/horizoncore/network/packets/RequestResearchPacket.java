package com.horizonpack.horizoncore.network.packets;
import com.horizonpack.horizoncore.core.HorizonCore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
public record RequestResearchPacket(ResourceLocation techId, BlockPos benchPos) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
    public static final Type<RequestResearchPacket> ID = new Type<>(new ResourceLocation(HorizonCore.MODID, "request_research"));
    public RequestResearchPacket(FriendlyByteBuf buf) { this(buf.readResourceLocation(), buf.readBlockPos()); }
    @Override public void write(FriendlyByteBuf buf) { buf.writeResourceLocation(techId); buf.writeBlockPos(benchPos); }
    @Override public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return ID; }
    public static void handleServer(RequestResearchPacket packet, IPayloadContext ctx) { /* Handle bench logic */ }
}