package org.styly.efm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallCrate extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape BLOCK_AABB = Block.box(1, 0, 2, 15, 5, 14);

    public SmallCrate() {
        super(Properties.ofFullCopy(Blocks.LODESTONE).noOcclusion().strength(2f,3f));
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));

    }
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (menuprovider != null) {
                pPlayer.openMenu(menuprovider);
                PiglinAi.angerNearbyPiglins(pPlayer, true);
            }

            return InteractionResult.CONSUME;
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        // this is where the properties are actually added to the state
        pBuilder.add(FACING);
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BLOCK_AABB;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SmallCrateTile(pPos,pState);
    }
    @Override
    protected @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
       return defaultBlockState().setValue(FACING,pContext.getHorizontalDirection());
    }
}
