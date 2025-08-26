package org.styly.efm.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.efm.EFM;
import org.styly.efm.block.SmallCrate;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EFM.MODID);

    public static final DeferredBlock<SmallCrate> SMALL_CRATE = BLOCKS.register("small_crate", SmallCrate::new);

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
