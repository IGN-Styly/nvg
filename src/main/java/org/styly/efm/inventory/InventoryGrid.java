package org.styly.efm.inventory;

public class InventoryGrid {

    private final int rows;
    private final int cols;
    private InventoryItem[][] grid;

    public InventoryGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new InventoryItem[rows][cols];
    }

    // Check if item fits at position
    public boolean canPlaceItem(
        InventoryItem item,
        int startRow,
        int startCol
    ) {
        for (int r = 0; r < item.getHeight(); r++) {
            for (int c = 0; c < item.getWidth(); c++) {
                int row = startRow + r;
                int col = startCol + c;
                if (
                    row >= rows || col >= cols || grid[row][col] != null
                ) return false;
            }
        }
        return true;
    }

    // Place item in grid
    public void placeItem(InventoryItem item, int startRow, int startCol) {
        if (!canPlaceItem(item, startRow, startCol)) return;
        for (int r = 0; r < item.getHeight(); r++) {
            for (int c = 0; c < item.getWidth(); c++) {
                grid[startRow + r][startCol + c] = item;
            }
        }
    }

    // Remove item from grid
    public void removeItem(InventoryItem item) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == item) grid[r][c] = null;
            }
        }
    }

    // Get rows count
    public int getRows() {
        return rows;
    }

    // Get columns count
    public int getCols() {
        return cols;
    }

    // Get item at position
    public InventoryItem getItemAt(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) {
            return null;
        }
        return grid[row][col];
    }
}
