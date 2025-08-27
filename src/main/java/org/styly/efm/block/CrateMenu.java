package org.styly.efm.block;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CrateMenu extends AbstractContainerMenu {

    private final Container container;

    public CrateMenu(
        int id,
        Inventory playerInventory,
        Container crateInventory
    ) {
        super(MenuType.GENERIC_3x3, id); // You can use a custom MenuType if needed
        this.container = crateInventory;
        crateInventory.startOpen(playerInventory.player);

        // Crate slots (1 row of 9)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(crateInventory, i, 8 + i * 18, 18));
        }

        // Player inventory slots (3 rows of 9)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(
                    new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        50 + row * 18
                    )
                );
            }
        }

        // Hotbar (1 row of 9)
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 108));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ItemStack copy = stack.copy();

            int crateSlots = 9;
            int inventoryStart = crateSlots;
            int inventoryEnd = this.slots.size();

            if (index < crateSlots) {
                // Moving from crate to player inventory
                if (
                    !this.moveItemStackTo(
                        stack,
                        inventoryStart,
                        inventoryEnd,
                        true
                    )
                ) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from player inventory to crate
                if (!this.moveItemStackTo(stack, 0, crateSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, stack);
            return copy;
        }

        return ItemStack.EMPTY;
    }
}
