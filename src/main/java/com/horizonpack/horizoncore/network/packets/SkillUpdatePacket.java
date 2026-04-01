package com.horizonpack.horizoncore.network.packets;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
public record SkillUpdatePacket(String skillName, int level, int xp) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
    public static final Type<SkillUpdatePacket> ID = new Type<>(new ResourceLocation(HorizonCore.MODID, "skill_update"));
    public SkillUpdatePacket(FriendlyByteBuf buf) { this(buf.readUtf(), buf.readInt(), buf.readInt()); }
    @Override public void write(FriendlyByteBuf buf) { buf.writeUtf(skillName); buf.writeInt(level); buf.writeInt(xp); }
    @Override public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return ID; }
    public static void handleClient(SkillUpdatePacket packet, IPayloadContext ctx) { /* Phase 6 */ }
}