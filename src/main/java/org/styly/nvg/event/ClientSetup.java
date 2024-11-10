package org.styly.nvg.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.styly.nvg.Nvg;
import org.styly.nvg.rederer.GPNVG_POST;
import org.styly.nvg.rederer.WPNVG_POST;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

@EventBusSubscriber(modid = Nvg.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(GPNVG_POST.INSTANCE);
        PostProcessHandler.addInstance(WPNVG_POST.INSTANCE);
    }
}
