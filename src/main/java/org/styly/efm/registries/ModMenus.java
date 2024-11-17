package org.styly.efm.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.efm.EFM;
import org.styly.efm.block.CrateMenu;

import java.util.function.Supplier;

public class ModMenus {
    // For some DeferredRegister<MenuType<?>> REGISTER
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, EFM.MODID);
    public static void register(IEventBus eventBus){
        REGISTER.register(eventBus);
    }
}
