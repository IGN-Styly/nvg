package org.styly.efm;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.styly.efm.registries.DataCompReg;
import org.styly.efm.registries.ModItemReg;
import org.styly.efm.registries.ModSounds;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EFM.MODID)
public class EFM {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "efm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EFM(IEventBus modEventBus, ModContainer modContainer) {
        ModItemReg.register(modEventBus);
        DataCompReg.register(modEventBus);
        ModSounds.register(modEventBus);

    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(EFM.MODID, path);
    }

}
