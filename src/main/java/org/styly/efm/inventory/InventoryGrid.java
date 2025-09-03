package org.styly.efm.inventory;

import org.styly.efm.EFM;

/**
 * Represents a grid structure for inventory items.
 * Manages the placement, removal, and querying of items in a 2D grid.
 */
public class InventoryGrid {

    private final int rows;
    private final int cols;
    private final InventoryItem[][] grid;

    /**
     * Creates a new inventory grid with the specified dimensions.
     *
     * @param rows Number of rows in the grid
     * @param cols Number of columns in the grid
     */
    public InventoryGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new InventoryItem[rows][cols];
    }

    /**
     * Checks if an item can be placed at the specified position.
     *
     * @param item     Item to place
     * @param startRow Starting row position
     * @param startCol Starting column position
     * @return true if the item can be placed, false otherwise
     */
    public boolean canPlaceItem(
            InventoryItem item,
            int startRow,
            int startCol
    ) {
        if (item == null) {
            return false;
        }

        int width = item.getWidth();
        int height = item.getHeight();

        // Check if item is within grid boundaries
        if (
                startRow < 0 ||
                        startCol < 0 ||
                        startRow + height > rows ||
                        startCol + width > cols
        ) {
            return false;
        }

        // Check if all required cells are empty
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[startRow + r][startCol + c] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Places an item in the grid at the specified position.
     *
     * @param item     Item to place
     * @param startRow Starting row position
     * @param startCol Starting column position
     * @return true if the item was placed, false otherwise
     */
    public boolean placeItem(InventoryItem item, int startRow, int startCol) {
        if (!canPlaceItem(item, startRow, startCol)) {
            EFM.LOGGER.info("{},{}", startRow, startCol);
            return false;
        }

        // Place item reference in all cells it occupies
        for (int r = 0; r < item.getHeight(); r++) {
            for (int c = 0; c < item.getWidth(); c++) {
                grid[startRow + r][startCol + c] = item;
            }
        }

        // Update item position
        return true;
    }

    /**
     * Removes an item from the grid.
     *
     * @param item Item to remove
     * @return true if the item was found and removed, false otherwise
     */
    public boolean removeItem(InventoryItem item) {
        if (item == null) {
            return false;
        }

        boolean found = false;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == item) {
                    grid[r][c] = null;
                    found = true;
                }
            }
        }

        return found;
    }

    /**
     * Gets the item at the specified position.
     *
     * @param row Row position
     * @param col Column position
     * @return The item at the position, or null if no item or position is invalid
     */
    public InventoryItem getItemAt(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) {
            return null;
        }
        return grid[row][col];
    }

    /**
     * Checks if there is an item at the specified position.
     *
     * @param row Row position
     * @param col Column position
     * @return true if there is an item at the position, false otherwise
     */
    public boolean hasItem(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) {
            return false;
        }
        return grid[row][col] != null;
    }

    /**
     * Check if an item with the given dimensions can fit at the specified position
     *
     * @param startRow Starting row position
     * @param startCol Starting column position
     * @param height   Height of the item in cells
     * @param width    Width of the item in cells
     * @return true if the item fits, false otherwise
     */
    public boolean canFit(int startRow, int startCol, int height, int width) {
        if (
                startRow < 0 ||
                        startCol < 0 ||
                        startRow + height > rows ||
                        startCol + width > cols
        ) {
            return false;
        }

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int row = startRow + r;
                int col = startCol + c;
                if (grid[row][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get columns count (alias for getCols for consistency)
     */
    public int getColumns() {
        return cols;
    }

    /**
     * Gets the top-left position of an item in the grid.
     *
     * @param item Item to find
     * @return int[2] array containing [row, col] or null if not found
     */
    public int[] findItemPosition(InventoryItem item) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == item) {
                    // Check if this is the top-left corner of the item
                    boolean isTopLeft = r <= 0 || grid[r - 1][c] != item;
                    if (c > 0 && grid[r][c - 1] == item) {
                        isTopLeft = false;
                    }

                    if (isTopLeft) {
                        return new int[]{r, c};
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks if an area with the given dimensions is empty.
     *
     * @param startRow Starting row position
     * @param startCol Starting column position
     * @param height   Height of the area
     * @param width    Width of the area
     * @return true if the area is empty, false otherwise
     */
    public boolean isAreaEmpty(
            int startRow,
            int startCol,
            int height,
            int width
    ) {
        // Check boundaries
        if (
                startRow < 0 ||
                        startCol < 0 ||
                        startRow + height > rows ||
                        startCol + width > cols
        ) {
            return false;
        }

        // Check if all cells are empty
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[startRow + r][startCol + c] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Clears the entire grid, removing all items.
     */
    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = null;
            }
        }
    }

    /**
     * Gets the number of rows in the grid.
     *
     * @return Number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the grid.
     *
     * @return Number of columns
     */
    public int getCols() {
        return cols;
    }
}
