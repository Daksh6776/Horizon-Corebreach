package com.horizonpack.horizoncore.network.packets;

import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.HorizonCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPlayerDataPacket(CompoundTag playerData) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
    public static final Type<SyncPlayerDataPacket> ID = new Type<>(new ResourceLocation(HorizonCore.MODID, "sync_player_data"));

    public SyncPlayerDataPacket(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(playerData);
    }

    @Override
    public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
        return ID;
    }

    public static void handleClient(SyncPlayerDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IHorizonPlayerData data = HorizonCapabilities.get(player);
                if (data != null) {
                    data.deserializeNBT(packet.playerData());
                }
            }
        });
    }
}