package org.styly.nvg.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.nvg.Nvg;

public class ModSounds {
    // Assuming that your mod id is examplemod
    public static final DeferredRegister<net.minecraft.sounds.SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Nvg.MODID);

    // All vanilla sounds use variable range events.
    public static final DeferredHolder<SoundEvent, SoundEvent> NVG_ON = SOUND_EVENTS.register(
            "nv_on", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Nvg.MODID, "nvg_on"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> NVG_OFF = SOUND_EVENTS.register(
            "nv_off", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Nvg.MODID, "nvg_off"))
    );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
