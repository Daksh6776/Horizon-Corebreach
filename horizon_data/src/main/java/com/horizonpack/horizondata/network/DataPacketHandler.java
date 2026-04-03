package com.horizonpack.horizondata.network;

import com.horizonpack.horizondata.core.HorizonData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = HorizonData.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataPacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Define a protocol version. Change this if you update your packet structures!
        final PayloadRegistrar registrar = event.registrar("1.0");

        // Register Server -> Client packets
        registrar.playToClient(
                SyncWorkshopPacket.TYPE,
                SyncWorkshopPacket.STREAM_CODEC,
                SyncWorkshopPacket::handle
        );

        registrar.playToClient(
                WorkshopProgressPacket.TYPE,
                WorkshopProgressPacket.STREAM_CODEC,
                WorkshopProgressPacket::handle
        );
    }
}