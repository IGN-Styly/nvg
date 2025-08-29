package org.styly.efm.inventory;

import net.minecraft.world.item.ItemStack;

/**
 * Represents an item that can be placed in the inventory grid.
 * Items have dimensions, an associated ItemStack, and can be rotatable.
 */
public class InventoryItem {

    private int width, height;
    private final int originalWidth, originalHeight;
    private ItemStack stack;
    private boolean rotatable;
    private boolean isRotated = false;
    private int gridRow = -1; // Position in grid (row)
    private int gridCol = -1; // Position in grid (column)

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
        this.originalWidth = this.width;
        this.originalHeight = this.height;
    }

    /**
     * Rotates the item by swapping width and height.
     * Each rotation toggles between normal and rotated state.
     *
     * @return true if rotation succeeded, false otherwise
     */
    public boolean rotate() {
        if (!rotatable) return false;

        // Toggle rotation state
        isRotated = !isRotated;

        // Swap width and height
        if (isRotated) {
            width = originalHeight;
            height = originalWidth;
        } else {
            width = originalWidth;
            height = originalHeight;
        }

        return true;
    }

    /**
     * Legacy method for compatibility. Same as rotate() but void return type.
     */
    public void rotate_legacy() {
        rotate();
    }

    /**
     * Sets the grid position of this item.
     *
     * @param row The row index in the grid
     * @param col The column index in the grid
     */
    public void setGridPosition(int row, int col) {
        this.gridRow = row;
        this.gridCol = col;
    }

    /**
     * Gets the row position of this item in the grid.
     *
     * @return The row index, or -1 if not placed in grid
     */
    public int getGridRow() {
        return gridRow;
    }

    /**
     * Gets the column position of this item in the grid.
     *
     * @return The column index, or -1 if not placed in grid
     */
    public int getGridCol() {
        return gridCol;
    }

    /**
     * Checks if the item is currently placed in a grid.
     *
     * @return true if the item has valid grid coordinates
     */
    public boolean isPlacedInGrid() {
        return gridRow >= 0 && gridCol >= 0;
    }

    /**
     * Checks if the item is currently rotated.
     *
     * @return true if dimensions are swapped, false otherwise
     */
    public boolean isRotated() {
        return isRotated;
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
    public int getOriginalWidth() {
        return originalWidth;
    }

    /**
     * Gets the original height of the item before any rotation.
     *
     * @return Original height in grid cells
     */
    public int getOriginalHeight() {
        return originalHeight;
    }

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

    /**
     * Resets the rotation to the default state.
     */
    public void resetRotation() {
        // Reset rotation state and restore original dimensions
        isRotated = false;
        width = originalWidth;
        height = originalHeight;
    }

    /**
     * Creates a copy of this inventory item.
     *
     * @return A new InventoryItem with the same properties
     */
    public InventoryItem copy() {
        InventoryItem copy = new InventoryItem(
            stack.copy(),
            originalWidth,
            originalHeight,
            rotatable
        );

        // Copy rotation state if needed
        if (isRotated) {
            copy.rotate();
        }

        return copy;
    }
}
