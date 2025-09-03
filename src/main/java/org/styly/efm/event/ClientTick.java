package org.styly.efm.event;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.styly.efm.EFM;
import org.styly.efm.components.nvgtoggle;
import org.styly.efm.network.ToggleRecord;
import org.styly.efm.rederer.GPNVG_POST;
import org.styly.efm.rederer.WPNVG_POST;
import org.styly.efm.registries.DataCompReg;

import java.util.Objects;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTick {
    private static final CameraType FP = CameraType.FIRST_PERSON;

    @SubscribeEvent
    public static void onClientTick(PlayerTickEvent.Post event) {
        Player entity = event.getEntity();
        ItemStack nvg = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (Minecraft.getInstance().options.getCameraType() != FP && nvg.has(DataCompReg.NVG_TOGGLE)) {
            nvgtoggle tk = nvg.get(DataCompReg.NVG_TOGGLE);
            if (tk.id() == 0 && tk.toggle()) {
                PacketDistributor.sendToServer(new ToggleRecord(0, 0));
                GPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
            if (tk.id() == 1 && tk.toggle()) {
                PacketDistributor.sendToServer(new ToggleRecord(0, 0));
                WPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
        }
        if (nvg.has(DataCompReg.NVG_TOGGLE)) {
            GPNVG_POST.INSTANCE.setActive(nvg.get(DataCompReg.NVG_TOGGLE).id() == 0 && Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            WPNVG_POST.INSTANCE.setActive(nvg.get(DataCompReg.NVG_TOGGLE).id() == 1 && Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
        } else {
            GPNVG_POST.INSTANCE.setActive(false);
            WPNVG_POST.INSTANCE.setActive(false);

        }
    }

}
