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
 * A lightweight packet to sync just the progress variables of a workshop for GUI rendering.
 */
public record WorkshopProgressPacket(BlockPos pos, int progress, int maxProgress) implements CustomPacketPayload {

    public static final Type<WorkshopProgressPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HorizonData.MODID, "workshop_progress"));

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkshopProgressPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, WorkshopProgressPacket::pos,
            ByteBufCodecs.INT, WorkshopProgressPacket::progress,
            ByteBufCodecs.INT, WorkshopProgressPacket::maxProgress,
            WorkshopProgressPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            BlockEntity be = level.getBlockEntity(pos);

            // TODO: Cast to your block entity and update progress variables
            /*
            if (be instanceof AbstractWorkshopBlockEntity workshop) {
                workshop.setProgress(this.progress);
                workshop.setMaxProgress(this.maxProgress);
            }
            */
        });
    }
}