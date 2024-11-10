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

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EFM.MODID);

    public static final DeferredItem<Item> NVG_DOWN = ITEMS.register("gpnvg", () -> new gpnvg(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().component(DataCompReg.NVG_TOGGLE, new nvgtoggle(false,0))));
    public static final DeferredItem<Item> NVG_WP = ITEMS.register("wpnvg", () -> new gpnvg(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().component(DataCompReg.NVG_TOGGLE, new nvgtoggle(false,1))));
    public static final DeferredItem<Item> NVG_WP2 = ITEMS.register("wpnvg2", () -> new gpnvg(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().component(DataCompReg.NVG_TOGGLE, new nvgtoggle(false,1))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
