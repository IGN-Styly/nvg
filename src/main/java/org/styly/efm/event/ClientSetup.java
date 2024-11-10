package org.styly.efm.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.styly.efm.EFM;
import org.styly.efm.rederer.GPNVG_POST;
import org.styly.efm.rederer.WPNVG_POST;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(GPNVG_POST.INSTANCE);
        PostProcessHandler.addInstance(WPNVG_POST.INSTANCE);
    }
}
