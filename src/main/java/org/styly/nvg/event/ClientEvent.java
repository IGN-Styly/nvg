package org.styly.nvg.event;

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
import org.styly.nvg.Nvg;
import org.styly.nvg.network.ToggleRecord;
import org.styly.nvg.rederer.GPNVG_POST;
import org.styly.nvg.rederer.WPNVG_POST;
import org.styly.nvg.registries.DataCompReg;
import org.styly.nvg.registries.ModItemReg;

import java.util.Objects;

import static org.styly.nvg.registries.KeyReg.NVG_MAPPING;

@EventBusSubscriber(modid = Nvg.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Player entity = Minecraft.getInstance().player;
        if (entity == null) {
            return;
        }
        ItemStack nvg = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (NVG_MAPPING.consumeClick() && (nvg.is(ModItemReg.NVG_DOWN) || nvg.is(ModItemReg.NVG_WP)) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            PacketDistributor.sendToServer(new ToggleRecord(0, 0));
            if (nvg.is(ModItemReg.NVG_DOWN)) {
                GPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
                WPNVG_POST.INSTANCE.setActive(Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            } else if (nvg.is(ModItemReg.NVG_WP)) {
                WPNVG_POST.INSTANCE.setActive(!Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
                GPNVG_POST.INSTANCE.setActive(Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle());
            }
        }

    }

}
