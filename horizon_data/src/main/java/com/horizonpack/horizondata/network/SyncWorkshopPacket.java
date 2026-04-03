package com.horizonpack.horizondata.network;

import com.horizonpack.horizondata.core.HorizonData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Syncs the entire relevant state of a workshop block entity from Server -> Client.
 */
public record SyncWorkshopPacket(BlockPos pos, int progress, int maxProgress, int fuelRemaining) implements CustomPacketPayload {

    // The unique ID for this payload type
    public static final Type<SyncWorkshopPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonData.MODID, "sync_workshop"));

    // The StreamCodec handles encoding and decoding automatically using Java 21 composites
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWorkshopPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncWorkshopPacket::pos,
            ByteBufCodecs.INT, SyncWorkshopPacket::progress,
            ByteBufCodecs.INT, SyncWorkshopPacket::maxProgress,
            ByteBufCodecs.INT, SyncWorkshopPacket::fuelRemaining,
            SyncWorkshopPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Handles the packet when received on the client side.
     */
    public void handle(IPayloadContext context) {
        // enqueueWork ensures the logic runs on the main game thread
        context.enqueueWork(() -> {
            Level level = context.player().level();
            BlockEntity be = level.getBlockEntity(pos);

            // TODO: Cast to your specific BlockEntity interface (e.g., IWorkshopBlockEntity)
            // and update its client-side variables so the UI renders correctly.
            /*
            if (be instanceof AbstractWorkshopBlockEntity workshop) {
                workshop.setProgress(this.progress);
                workshop.setMaxProgress(this.maxProgress);
                workshop.setFuelRemaining(this.fuelRemaining);
            }
            */
        });
    }
}