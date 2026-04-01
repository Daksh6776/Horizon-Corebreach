package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.data.ResearchProject;
import com.horizonpack.horizoncore.data.WorldAgeData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestResearchPacket(ResourceLocation techId, BlockPos benchPos) implements CustomPacketPayload {

    public static final Type<RequestResearchPacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "request_research"));

    public static final StreamCodec<FriendlyByteBuf, RequestResearchPacket> STREAM_CODEC = StreamCodec.ofMember(
            RequestResearchPacket::write, RequestResearchPacket::new
    );

    public RequestResearchPacket(FriendlyByteBuf buf) {
        this(buf.readResourceLocation(), buf.readBlockPos());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(techId);
        buf.writeBlockPos(benchPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

    // This handles what the SERVER does when it receives the click!
    public void handleServer(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                // 1. Fetch the Technology Data to know how long it takes
                com.horizonpack.horizoncore.data.TechnologyData tech = null;
                for (var entry : com.horizonpack.horizoncore.core.HorizonRegistries.TECHNOLOGIES.getEntries()) {
                    if (entry.getId().equals(techId)) tech = entry.get();
                }

                if (tech != null) {
                    // 2. Create the Active Research Project [cite: 193-196]
                    ResearchProject project = new ResearchProject(techId, tech.getResearchTimeTicks(), player.getUUID());

                    // 3. Save it to the World Data so it processes over time [cite: 265, 294-295]
                    WorldAgeData.get(player.serverLevel()).setActiveResearch(benchPos, project);

                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Research Started: " + techId.getPath()));
                }
            }
        });
    }
}