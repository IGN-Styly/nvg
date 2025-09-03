package org.styly.efm.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.efm.EFM;
import org.styly.efm.block.SmallCrateTile;

public class ModTile {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EFM.MODID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SmallCrateTile>> SMALL_CRATE_TILE = BLOCK_ENTITY_TYPES.register(
            "small_crate_tile",
            // The block entity type.
            () -> BlockEntityType.Builder.of(SmallCrateTile::new, ModBlocks.SMALL_CRATE.get()).build(null)
    );


}
