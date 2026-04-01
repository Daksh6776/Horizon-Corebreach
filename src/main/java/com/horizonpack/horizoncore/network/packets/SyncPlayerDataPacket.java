package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPlayerDataPacket(CompoundTag playerData) implements CustomPacketPayload {
    public static final Type<SyncPlayerDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonCore.MODID, "sync_player_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SyncPlayerDataPacket::playerData,
            SyncPlayerDataPacket::new
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleClient(SyncPlayerDataPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IHorizonPlayerData data = HorizonCapabilities.get(player);
                if (data != null) data.deserializeNBT(payload.playerData());
            }
        });
    }
}