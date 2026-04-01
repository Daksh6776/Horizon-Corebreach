package com.horizonpack.horizoncore.voice;

import com.horizonpack.horizoncore.data.HorizonAge;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class HorizonVoicePlugin implements VoicechatPlugin {

    @Nullable
    public static VoicechatServerApi voiceApi;

    @Override
    public String getPluginId() { return "horizoncore"; }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, event -> {
            voiceApi = event.getVoicechat();
        });
    }

    public static void updatePlayerVoice(ServerPlayer player, HorizonAge age) {
        if (voiceApi == null) return;

        // In SVC 2.4.0, per-player distance isn't standard.
        // We will control voice via Groups instead (e.g., locking Stone Age players out of global radios).
        // This stops the compilation error while keeping the plugin active.
    }
}