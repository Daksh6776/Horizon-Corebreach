package com.horizonpack.horizoncore.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class HorizonKeybinds {
    public static final String CATEGORY = "key.category.horizoncore";

    // Creates a new keybind defaulting to the TAB key
    public static final KeyMapping SHOW_DETAILS = new KeyMapping(
            "key.horizoncore.show_details",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB,
            CATEGORY
    );
}