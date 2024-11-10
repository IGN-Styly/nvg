package org.styly.nvg.registries;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import org.styly.nvg.Nvg;

@EventBusSubscriber(value = Dist.CLIENT, modid = Nvg.MODID, bus = EventBusSubscriber.Bus.MOD)
public class KeyReg {

    // Key mapping is lazily initialized so it doesn't exist until it is registered
    public static final KeyMapping NVG_MAPPING = new KeyMapping(
            "key.nvg.toggle", // Will be localized using this translation key
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_G, // Default key is G
            "key.categories.misc" // Mapping will be in the misc category
    );

    @SubscribeEvent
    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
        event.register(NVG_MAPPING);
    }
}
