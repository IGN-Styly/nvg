package org.styly.efm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LongSmallCrate extends BaseEntityBlock {
    public static final VoxelShape BLOCK_AABB = Block.box(0, 0, 0, 16, 15, 16);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

    public LongSmallCrate() {
        super(Properties.ofFullCopy(Blocks.LODESTONE).noOcclusion().strength(2f,3f));
        this.registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(PART,BedPart.FOOT));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        // this is where the properties are actually added to the state
        pBuilder.add(FACING, PART);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            BlockPos relative = pos.relative(state.getValue(FACING));
            worldIn.setBlock(relative, state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
            worldIn.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(worldIn, pos, Block.UPDATE_ALL);
        }
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    private static Direction getNeighbourDirection(BedPart bedPart, Direction direction) {
        return bedPart == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos relative = clickedPos.relative(direction);
        Level level = context.getLevel();
        if (level.getBlockState(relative).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(relative)) {
            return this.defaultBlockState().setValue(FACING, direction);
        }
        return null;
    }
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart bedPart = blockState.getValue(PART);
            if (bedPart == BedPart.FOOT) {
                BlockPos blockpos = pos.relative(getNeighbourDirection(bedPart, blockState.getValue(FACING)));
                BlockState blockstate = level.getBlockState(blockpos);
                if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(level, pos, blockState, player);
        return blockState;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (direction == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return facingState.is(this) && facingState.getValue(PART) != state.getValue(PART) ? state : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
        }
    }
    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BLOCK_AABB;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LongSmallCrateTile(pPos,pState);
    }
}
