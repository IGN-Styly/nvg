package org.styly.nvg;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.styly.nvg.registries.DataCompReg;
import org.styly.nvg.registries.ModItemReg;
import org.styly.nvg.registries.ModSounds;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Nvg.MODID)
public class Nvg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nvg";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Nvg(IEventBus modEventBus, ModContainer modContainer) {
        ModItemReg.register(modEventBus);
        DataCompReg.register(modEventBus);
        ModSounds.register(modEventBus);

    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(Nvg.MODID, path);
    }

}
