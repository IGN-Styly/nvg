package org.styly.efm.inventory;

import net.minecraft.world.item.ItemStack;

/**
 * Represents an item that can be placed in the inventory grid.
 * Items have dimensions, an associated ItemStack, and can be rotatable.
 */
public class InventoryItem {

    private int width, height;
    private ItemStack stack;
    private boolean rotatable;

    /**
     * Creates a new inventory item with the specified properties.
     *
     * @param stack The Minecraft ItemStack associated with this inventory item
     * @param width Width of the item in grid cells
     * @param height Height of the item in grid cells
     * @param rotatable Whether the item can be rotated
     */
    public InventoryItem(
        ItemStack stack,
        int width,
        int height,
        boolean rotatable
    ) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.rotatable = rotatable;
    }

    /**
     * Rotates the item by swapping width and height.
     * Each rotation toggles between normal and rotated state.
     *
     * @return true if rotation succeeded, false otherwise
     */
    public boolean rotate() {
        var x = this.height;
        this.height=width;
        this.width=x;

        return true;
    }



    /**
     * Gets the current width of the item (may be affected by rotation).
     *
     * @return Current width in grid cells
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the current height of the item (may be affected by rotation).
     *
     * @return Current height in grid cells
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the original width of the item before any rotation.
     *
     * @return Original width in grid cells
     */


    /**
     * Gets the Minecraft ItemStack associated with this inventory item.
     *
     * @return The ItemStack
     */
    public ItemStack getStack() {
        return stack;
    }

    /**
     * Gets the underlying ItemStack for compatibility with old code.
     *
     * @return The raw ItemStack
     */
    public ItemStack getItemStack() {
        return stack;
    }

    /**
     * Sets the underlying ItemStack.
     *
     * @param stack The new ItemStack to use
     */
    public void setItemStack(ItemStack stack) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
    }

    /**
     * Gets the quantity of items in the stack.
     *
     * @return Item count
     */
    public int getAmount() {
        return stack.getCount();
    }

    /**
     * Changes the quantity of items in the stack.
     *
     * @param count New item count
     */
    public void setAmount(int count) {
        stack.setCount(Math.max(0, count));
    }

    /**
     * Checks if this item can be rotated.
     *
     * @return true if the item is rotatable
     */
    public boolean isRotatable() {
        return rotatable;
    }

}
