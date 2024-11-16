package org.styly.efm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.styly.efm.registries.ModTile;

public class SmallCrateTile extends BlockEntity {
    public SmallCrateTile( BlockPos pPos, BlockState pBlockState) {
        super(ModTile.SMALL_CRATE_TILE.get(), pPos, pBlockState);
    }

}
