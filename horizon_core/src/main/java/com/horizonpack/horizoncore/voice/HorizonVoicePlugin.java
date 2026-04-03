package com.horizonpack.horizoncore.voice;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.server.level.ServerPlayer;
import com.horizonpack.horizoncore.data.HorizonAge; // Or wherever your HorizonAge is located

@ForgeVoicechatPlugin
public class HorizonVoicePlugin implements VoicechatPlugin {

    public static VoicechatApi VOICECHAT_API;
    public static VoicechatServerApi VOICECHAT_SERVER_API;

    @Override
    public String getPluginId() {
        return "horizoncore_voicechat";
    }

    @Override
    public void initialize(VoicechatApi api) {
        VOICECHAT_API = api;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        VOICECHAT_SERVER_API = event.getVoicechat();
    }

    /**
     * Called from ModEvents.java to update a player's voice range/settings based on the civilization age.
     */
    /**
     * Called from ModEvents.java to update a player's voice range/settings based on the civilization age.
     */
    public static void updatePlayerVoice(ServerPlayer player, HorizonAge age) {
        // 1. Safety check: Ensure the server API is actually loaded before doing anything
        if (VOICECHAT_SERVER_API == null) return;

        // 2. We can grab the player's active voice connection (returns null if they don't have the mod installed)
        var connection = VOICECHAT_SERVER_API.getConnectionOf(player.getUUID());

        if (connection != null) {
            // Your logic here to alter their voice based on the Age!
            // For example, broadcasting a packet to nearby players, or modifying group distances.
        }
    }
}