package org.styly.efm.event;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.styly.efm.EFM;
import org.styly.efm.rederer.GPNVG_POST;
import org.styly.efm.rederer.WPNVG_POST;
import org.styly.efm.registries.ModTile;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(GPNVG_POST.INSTANCE);
        PostProcessHandler.addInstance(WPNVG_POST.INSTANCE);
    }
    @SubscribeEvent
    public static void registerTileRenderer(EntityRenderersEvent.RegisterRenderers event){
    }
    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event){
        event.register(new ModelResourceLocation(EFM.id("block/small_crate"),"standalone"));
    }

}
