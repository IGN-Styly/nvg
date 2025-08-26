package org.styly.efm;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.styly.efm.registries.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EFM.MODID)
public class EFM {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "efm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EFM(IEventBus eventBus, ModContainer modContainer) {
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        DataCompReg.register(eventBus);
        ModSounds.register(eventBus);
        ModTile.register(eventBus);
        ModMenus.register(eventBus);
        TabRegistry.register(eventBus);
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(EFM.MODID, path);
    }

}
