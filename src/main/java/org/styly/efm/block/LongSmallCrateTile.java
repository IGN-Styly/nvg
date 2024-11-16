package org.styly.efm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.styly.efm.registries.ModBlocks;
import org.styly.efm.registries.ModTile;

public class LongSmallCrateTile extends BlockEntity {
    public LongSmallCrateTile( BlockPos pPos, BlockState pBlockState) {
        super(ModTile.LONG_SMALL_CRATE_TILE.get(), pPos, pBlockState);
    }
}
