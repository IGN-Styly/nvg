package org.styly.efm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.styly.efm.registries.ModTile;

public class SmallCrateTile extends BaseContainerBlockEntity {
    public static final int SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public SmallCrateTile( BlockPos pPos, BlockState pBlockState) {
        super(ModTile.SMALL_CRATE_TILE.get(), pPos, pBlockState);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("efm.ui.small_crate");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items=pItems;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new DispenserMenu(pContainerId,pInventory);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }
}
