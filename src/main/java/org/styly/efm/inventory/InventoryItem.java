package org.styly.efm.inventory;

import net.minecraft.world.item.ItemStack;

public class InventoryItem {
    private int width, height;
    private ItemStack stack;
    private boolean rotatable;

    public InventoryItem(ItemStack stack, int width, int height, boolean rotatable) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        this.width = width;
        this.height = height;
        this.rotatable = rotatable;
    }

    public void rotate() {
        if (!rotatable) return;
        int temp = width;
        width = height;
        height = temp;
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public ItemStack getStack() { return stack; }
    public int getAmount() { return stack.getCount(); }
}
