package org.styly.nvg.event;

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
import org.styly.nvg.Nvg;
import org.styly.nvg.network.ToggleRecord;
import org.styly.nvg.rederer.GPNVG_POST;
import org.styly.nvg.rederer.WPNVG_POST;
import org.styly.nvg.registries.DataCompReg;
import org.styly.nvg.registries.ModItemReg;

import java.util.Objects;

@EventBusSubscriber(modid = Nvg.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTick {
    private static final CameraType FP = CameraType.FIRST_PERSON;
    private static CameraType PREV_CAM = CameraType.FIRST_PERSON;

    @SubscribeEvent
    public static void onClientTick(PlayerTickEvent.Post event) {
        Player entity = event.getEntity();
        ItemStack nvg = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (Minecraft.getInstance().options.getCameraType() != FP) {
            PREV_CAM = Minecraft.getInstance().options.getCameraType();
            if (nvg.is(ModItemReg.NVG_DOWN) && nvg.get(DataCompReg.NVG_TOGGLE).toggle()) {
                PacketDistributor.sendToServer(new ToggleRecord(0, 0));
                GPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
            if (nvg.is(ModItemReg.NVG_WP) && nvg.get(DataCompReg.NVG_TOGGLE).toggle()) {
                PacketDistributor.sendToServer(new ToggleRecord(0, 0));
                WPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
        }
        GPNVG_POST.INSTANCE.setActive(nvg.is(ModItemReg.NVG_DOWN) && Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
        WPNVG_POST.INSTANCE.setActive(nvg.is(ModItemReg.NVG_WP) && Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
    }

}
