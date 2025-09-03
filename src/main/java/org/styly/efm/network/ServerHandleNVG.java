package org.styly.efm.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.styly.efm.components.nvgtoggle;
import org.styly.efm.registries.DataCompReg;
import org.styly.efm.registries.ModSounds;

import java.util.Objects;

@EventBusSubscriber(
        bus = EventBusSubscriber.Bus.MOD
)
public class ServerHandleNVG {
    public static void handle(final ToggleRecord data, final IPayloadContext context) {
        ServerLevel world = (ServerLevel) context.player().level();
        Player player = context.player();
        if (player.getItemBySlot(EquipmentSlot.HEAD).has(DataCompReg.NVG_TOGGLE)) {
            ItemStack nvg = player.getItemBySlot(EquipmentSlot.HEAD);
            Boolean status = Objects.requireNonNull(nvg.get(DataCompReg.NVG_TOGGLE)).toggle();
            nvg.set(DataCompReg.NVG_TOGGLE, new nvgtoggle(!status, nvg.get(DataCompReg.NVG_TOGGLE).id(), nvg.get(DataCompReg.NVG_TOGGLE).overlay()));
            if (!status) {
                world.playSound(null, player.blockPosition(), ModSounds.NVG_ON.get(), SoundSource.PLAYERS, 1, 1);
            }
        }

    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.commonToServer(ToggleRecord.TYPE, ToggleRecord.STREAM_CODEC, new MainThreadPayloadHandler<>(ServerHandleNVG::handle));
    }
}

