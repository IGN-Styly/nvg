package org.styly.efm.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static org.styly.efm.EFM.MODID;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,MODID);

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> EFM = CREATIVE_TABS.register("efm",()-> CreativeModeTab.builder().title(Component.translatable("itemGroup.efm")).icon(()->ModItems.NVG_WP.get().getDefaultInstance()).displayItems(((parameters, output) ->{
        output.accept(ModItems.SMALL_CRATE_ITEM);
        output.accept(ModItems.NVG_GP);
        output.accept(ModItems.NVG_GP_SP);
        output.accept(ModItems.NVG_WP);
    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

}
