package org.styly.efm.registries;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.efm.EFM;
import org.styly.efm.components.nvgtoggle;
import org.styly.efm.item.gpnvg;

public class ModItemReg {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EFM.MODID);

    public static final DeferredItem<Item> NVG_DOWN = ITEMS.register("gpnvg", () -> new gpnvg(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().component(DataCompReg.NVG_TOGGLE, new nvgtoggle(false))));
    public static final DeferredItem<Item> NVG_WP = ITEMS.register("wpnvg", () -> new gpnvg(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().component(DataCompReg.NVG_TOGGLE, new nvgtoggle(false))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
