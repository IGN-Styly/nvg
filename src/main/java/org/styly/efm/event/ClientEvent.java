package org.styly.efm.event;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.styly.efm.EFM;
import org.styly.efm.components.nvgtoggle;
import org.styly.efm.inventory.EFMInventoryScreen;
import org.styly.efm.network.ToggleRecord;
import org.styly.efm.rederer.GPNVG_POST;
import org.styly.efm.rederer.WPNVG_POST;
import org.styly.efm.registries.DataCompReg;

import java.util.Objects;

import static org.styly.efm.registries.KeyReg.INV_MAPPING;
import static org.styly.efm.registries.KeyReg.NVG_MAPPING;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (INV_MAPPING.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new EFMInventoryScreen());


        }
        Player entity = Minecraft.getInstance().player;
        if (entity == null) {
            return;
        }
        ItemStack nvg = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (NVG_MAPPING.consumeClick() && (nvg.has(DataCompReg.NVG_TOGGLE)) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            PacketDistributor.sendToServer(new ToggleRecord(0, 0));
            nvgtoggle nt = nvg.get(DataCompReg.NVG_TOGGLE);
            if (nt.id() == 0) {
                GPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
                WPNVG_POST.INSTANCE.setActive(Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            } else if (nt.id() == 1) {
                WPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
                GPNVG_POST.INSTANCE.setActive(Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
        }

    }

}
