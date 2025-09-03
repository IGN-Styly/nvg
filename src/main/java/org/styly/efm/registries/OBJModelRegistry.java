package org.styly.efm.registries;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.styly.efm.EFM;
import team.lodestar.lodestone.registry.client.LodestoneOBJModels;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.modifier.modifiers.TriangulateModifier;

import static org.styly.efm.EFM.id;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OBJModelRegistry {
    public static final ObjModel longSmallCrate = ObjModel.Builder.of(id("long_small_crate")).withModifiers(
            queue -> {
                queue.queueModifier(new TriangulateModifier());
            }
    ).build();

    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
        LodestoneOBJModels.register(longSmallCrate);

    }
}
