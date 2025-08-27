package org.styly.efm.inventory;

import net.minecraft.world.item.ItemStack;

public class InventoryItem {

    private int width, height;
    private final int originalWidth, originalHeight; // Store original dimensions
    private ItemStack stack;
    private boolean rotatable;
    private boolean isRotated = false; // true = rotated (swapped dimensions)

    public InventoryItem(
        ItemStack stack,
        int width,
        int height,
        boolean rotatable
    ) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        this.width = width;
        this.height = height;
        this.rotatable = rotatable;
        this.originalWidth = width; // Store original dimensions
        this.originalHeight = height;
    }

    /**
     * Rotates the item by swapping width and height.
     * Each rotation toggles between normal and rotated state.
     */
    public void rotate() {
        if (!rotatable) return;

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
    }

    /**
     * Checks if the item is currently rotated.
     *
     * @return true if dimensions are swapped, false otherwise
     */
    public boolean isRotated() {
        return isRotated;
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getAmount() {
        return stack.getCount();
    }

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
}
