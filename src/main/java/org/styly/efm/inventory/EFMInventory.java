package org.styly.efm.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Main inventory manager for Escape from Tarkov-like inventory system.
 * Handles rendering, scrolling, and inventory management.
 */
@OnlyIn(Dist.CLIENT)
public class EFMInventory {

    // Color constants for grid rendering
    private static final int COLOR_GRID_BORDER = 0xFF555555; // Dark gray for borders
    private static final int COLOR_GRID_BACKGROUND = 0xFF333333; // Darker gray for background
    private static final int COLOR_HOVER_HIGHLIGHT = 0x33FFFFFF; // Semi-transparent white for hover
    private static final int COLOR_ITEM_BACKGROUND = 0xFF444444; // Medium gray for item backgrounds
    private static final int COLOR_GRID_OUTLINE = 0xFFAAAAAA; // Lighter gray for grid outline

    // Visual constants
    private static final int CELL_SIZE = 32; // Size of each grid cell in pixels
    private static final int GRID_BORDER = 1; // Border size between cells

    // The actual inventory grid
    private InventoryGrid grid;

    // List of all items in this inventory
    private final List<InventoryItem> items = new ArrayList<>();

    // Scroll position for large inventories
    private int scrollX = 0;
    private int scrollY = 0;

    // Max scroll values
    private int maxScrollX = 0;
    private int maxScrollY = 0;

    // Visual area dimensions (in pixels)
    private int visibleWidth;
    private int visibleHeight;

    // Currently dragged item
    private InventoryItem draggedItem = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    // Currently hovered grid position
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    /**
     * Creates a new inventory with specified grid dimensions and visible area
     *
     * @param rows Number of rows in the inventory grid
     * @param cols Number of columns in the inventory grid
     * @param visibleWidth Visible width of the inventory in pixels
     * @param visibleHeight Visible height of the inventory in pixels
     */
    public EFMInventory(
        int rows,
        int cols,
        int visibleWidth,
        int visibleHeight
    ) {
        this.grid = new InventoryGrid(rows, cols);
        this.visibleWidth = visibleWidth;
        this.visibleHeight = visibleHeight;

        // Calculate maximum scroll values
        updateScrollLimits();
    }

    /**
     * Gets the total width of the grid in pixels
     */
    public int getTotalGridWidth() {
        // Just cells and inter-cell borders (consistent with item placement)
        return grid.getCols() * CELL_SIZE + (grid.getCols() - 1) * GRID_BORDER;
    }

    /**
     * Gets the total height of the grid in pixels
     */
    public int getTotalGridHeight() {
        // Just cells and inter-cell borders (consistent with item placement)
        return grid.getRows() * CELL_SIZE + (grid.getRows() - 1) * GRID_BORDER;
    }

    /**
     * Updates the maximum scroll values based on grid size and visible area
     */
    private void updateScrollLimits() {
        // Calculate exact max scroll values to show all grid cells including borders
        this.maxScrollX = Math.max(0, getTotalGridWidth() - visibleWidth);
        this.maxScrollY = Math.max(0, getTotalGridHeight() - visibleHeight);
    }

    /**
     * Renders the inventory grid and items
     *
     * @param guiGraphics GuiGraphics object for rendering
     * @param x Left position of the inventory
     * @param y Top position of the inventory
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     */
    public void render(
        GuiGraphics guiGraphics,
        int x,
        int y,
        int mouseX,
        int mouseY
    ) {
        // Ensure pixel-perfect rendering
        x = (int) Math.floor(x);
        y = (int) Math.floor(y);

        // Save the state
        guiGraphics.pose().pushPose();

        // Create a scissor to prevent rendering outside the visible area
        // But add a small margin for the borders
        guiGraphics.enableScissor(
            x - GRID_BORDER,
            y - GRID_BORDER,
            x + visibleWidth + GRID_BORDER,
            y + visibleHeight + GRID_BORDER
        );

        // Set translation to exact pixel boundaries
        guiGraphics.pose().translate(x, y, 0);

        // Draw the grid outline and background
        renderGrid(guiGraphics, 0, 0);

        // Render items in the grid after the background
        renderItems(guiGraphics, 0, 0);

        // Render dragged item (if any)
        if (draggedItem != null) {
            renderDraggedItem(
                guiGraphics,
                (mouseX - x) - dragOffsetX,
                (mouseY - y) - dragOffsetY
            );
        }

        // Render hover highlight
        renderHoverHighlight(guiGraphics, 0, 0);

        // Restore scissor
        guiGraphics.disableScissor();

        // Restore the state
        guiGraphics.pose().popPose();
    }

    /**
     * Renders the grid background
     */
    private void renderGrid(GuiGraphics guiGraphics, int x, int y) {
        // Calculate total grid dimensions
        int totalWidth = getTotalGridWidth();
        int totalHeight = getTotalGridHeight();

        // Draw outer border box with consistent width all around
        guiGraphics.fill(
            x - scrollX - GRID_BORDER,
            y - scrollY - GRID_BORDER,
            x + totalWidth - scrollX + GRID_BORDER,
            y + totalHeight - scrollY + GRID_BORDER,
            COLOR_GRID_OUTLINE
        );

        // Draw inner grid border (to fill background)
        guiGraphics.fill(
            x - scrollX,
            y - scrollY,
            x + totalWidth - scrollX,
            y + totalHeight - scrollY,
            COLOR_GRID_BORDER
        );

        // Calculate visible grid cells
        int startCol = Math.max(0, scrollX / (CELL_SIZE + GRID_BORDER));
        int startRow = Math.max(0, scrollY / (CELL_SIZE + GRID_BORDER));
        int endCol = Math.min(
            grid.getCols(),
            startCol + (visibleWidth / CELL_SIZE) + 2
        );
        int endRow = Math.min(
            grid.getRows(),
            startRow + (visibleHeight / CELL_SIZE) + 2
        );

        // Draw each visible cell
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                // Calculate cell position with consistent spacing
                int cellX = x + col * (CELL_SIZE + GRID_BORDER) - scrollX;
                int cellY = y + row * (CELL_SIZE + GRID_BORDER) - scrollY;

                // Draw cell background
                guiGraphics.fill(
                    cellX,
                    cellY,
                    cellX + CELL_SIZE,
                    cellY + CELL_SIZE,
                    COLOR_GRID_BACKGROUND
                );
            }
        }

        // Draw top border
        guiGraphics.fill(
            x - scrollX,
            y - scrollY - GRID_BORDER,
            x + totalWidth - scrollX,
            y - scrollY,
            COLOR_GRID_BORDER
        );

        // Draw left border
        guiGraphics.fill(
            x - scrollX - GRID_BORDER,
            y - scrollY,
            x - scrollX,
            y + totalHeight - scrollY,
            COLOR_GRID_BORDER
        );

        // Draw right border
        guiGraphics.fill(
            x + totalWidth - scrollX,
            y - scrollY,
            x + totalWidth + GRID_BORDER - scrollX,
            y + totalHeight - scrollY,
            COLOR_GRID_BORDER
        );

        // Draw bottom border
        guiGraphics.fill(
            x - scrollX,
            y + totalHeight - scrollY,
            x + totalWidth - scrollX,
            y + totalHeight + GRID_BORDER - scrollY,
            COLOR_GRID_BORDER
        );

        // Draw corners
        // top-left corner
        guiGraphics.fill(
            x - scrollX - GRID_BORDER,
            y - scrollY - GRID_BORDER,
            x - scrollX,
            y - scrollY,
            COLOR_GRID_BORDER
        );

        // top-right corner
        guiGraphics.fill(
            x + totalWidth - scrollX,
            y - scrollY - GRID_BORDER,
            x + totalWidth + GRID_BORDER - scrollX,
            y - scrollY,
            COLOR_GRID_BORDER
        );

        // bottom-left corner
        guiGraphics.fill(
            x - scrollX - GRID_BORDER,
            y + totalHeight - scrollY,
            x - scrollX,
            y + totalHeight + GRID_BORDER - scrollY,
            COLOR_GRID_BORDER
        );

        // bottom-right corner
        guiGraphics.fill(
            x + totalWidth - scrollX,
            y + totalHeight - scrollY,
            x + totalWidth + GRID_BORDER - scrollX,
            y + totalHeight + GRID_BORDER - scrollY,
            COLOR_GRID_BORDER
        );
    }

    /**
     * Renders all items in the grid
     */
    private void renderItems(GuiGraphics guiGraphics, int x, int y) {
        for (InventoryItem item : items) {
            // Find item position in grid
            Optional<int[]> position = findItemPosition(item);
            if (position.isEmpty() || item == draggedItem) continue;

            int[] pos = position.get();

            // Use consistent cell positioning for items
            int itemX = x + pos[1] * (CELL_SIZE + GRID_BORDER) - scrollX;
            int itemY = y + pos[0] * (CELL_SIZE + GRID_BORDER) - scrollY;

            // Check if item is visible
            if (
                itemX + item.getWidth() * CELL_SIZE < x ||
                itemY + item.getHeight() * CELL_SIZE < y ||
                itemX > x + visibleWidth ||
                itemY > y + visibleHeight
            ) {
                continue;
            }

            renderItemAt(guiGraphics, item, itemX, itemY);
        }
    }

    /**
     * Renders an item at the specified position
     */
    private void renderItemAt(
        GuiGraphics guiGraphics,
        InventoryItem item,
        int x,
        int y
    ) {
        // Get the item dimensions based on its rotation state
        int width = item.getWidth() * CELL_SIZE;
        int height = item.getHeight() * CELL_SIZE;

        // Draw item background - ensure pixel-perfect alignment
        // For multi-cell items, ensure we cover the border space
        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);

        // Calculate actual pixels needed for multi-cell items
        int actualWidth = width;
        int actualHeight = height;
        if (item.getWidth() > 1) {
            actualWidth =
                item.getWidth() * CELL_SIZE +
                (item.getWidth() - 1) * GRID_BORDER;
        }
        if (item.getHeight() > 1) {
            actualHeight =
                item.getHeight() * CELL_SIZE +
                (item.getHeight() - 1) * GRID_BORDER;
        }

        guiGraphics.fill(
            ix,
            iy,
            ix + actualWidth,
            iy + actualHeight,
            COLOR_ITEM_BACKGROUND
        );

        // Draw the actual Minecraft item
        // Center the item in its grid space
        int itemCenterX = ix + (width / 2);
        int itemCenterY = iy + (height / 2);

        // Calculate scale based on item size
        float scale = 1.0f;
        if (item.getWidth() > 1 || item.getHeight() > 1) {
            // For larger items, scale up (but not too much)
            scale = Math.min(
                Math.min(item.getWidth(), item.getHeight()) * 0.7f,
                2.0f
            );
        }

        // Save current pose
        guiGraphics.pose().pushPose();

        // Translate to center position
        guiGraphics.pose().translate(itemCenterX, itemCenterY, 0);
        // Scale the item
        guiGraphics.pose().scale(scale, scale, 1.0f);
        // Translate back for rendering centered
        guiGraphics.pose().translate(-8, -8, 0);

        // Render the scaled item
        guiGraphics.renderItem(item.getStack(), 0, 0);

        // Render item count if stack size > 1
        if (item.getAmount() > 1) {
            guiGraphics.renderItemDecorations(
                Minecraft.getInstance().font,
                item.getStack(),
                0,
                0,
                String.valueOf(item.getAmount())
            );
        }

        // Restore pose
        guiGraphics.pose().popPose();
    }

    /**
     * Renders the currently dragged item
     */
    private void renderDraggedItem(GuiGraphics guiGraphics, int x, int y) {
        // Make dragged item semi-transparent
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);

        renderItemAt(guiGraphics, draggedItem, x, y);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a highlight for the hovered grid position
     */
    private void renderHoverHighlight(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredRow < 0 || hoveredCol < 0) return;

        // Use consistent cell positioning for highlight
        int highlightX = x + hoveredCol * (CELL_SIZE + GRID_BORDER) - scrollX;
        int highlightY = y + hoveredRow * (CELL_SIZE + GRID_BORDER) - scrollY;

        // Draw hover highlight
        RenderSystem.enableBlend();

        // Draw a semi-transparent highlight over the cell
        // Use exact positioning
        guiGraphics.fill(
            highlightX,
            highlightY,
            highlightX + CELL_SIZE,
            highlightY + CELL_SIZE,
            COLOR_HOVER_HIGHLIGHT
        );

        RenderSystem.disableBlend();
    }

    /**
     * Handles mouse scroll events
     *
     * @param x Left position of the inventory
     * @param y Top position of the inventory
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     * @param delta Scroll delta
     */
    public void mouseScroll(
        int x,
        int y,
        int mouseX,
        int mouseY,
        double delta
    ) {
        if (
            mouseX >= x &&
            mouseX < x + visibleWidth &&
            mouseY >= y &&
            mouseY < y + visibleHeight
        ) {
            // Vertical scrolling with mouse wheel
            setScrollY(scrollY - (int) (delta * 15));

            // With SHIFT pressed, do horizontal scrolling
            if (hasShiftDown()) {
                setScrollX(scrollX - (int) (delta * 15));
            }
        }
    }

    /**
     * Sets the X scroll position, clamping to valid range
     */
    public void setScrollX(int newScrollX) {
        this.scrollX = Math.max(0, Math.min(maxScrollX, newScrollX));
    }

    /**
     * Sets the Y scroll position, clamping to valid range
     */
    public void setScrollY(int newScrollY) {
        this.scrollY = Math.max(0, Math.min(maxScrollY, newScrollY));
    }

    /**
     * Helper method to check if shift key is down
     */
    private boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }

    /**
     * Handle mouse clicked event
     *
     * @param x Left position of the inventory
     * @param y Top position of the inventory
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     * @param button Mouse button (0 = left, 1 = right)
     * @return True if the click was handled
     */
    public boolean mouseClicked(
        int x,
        int y,
        int mouseX,
        int mouseY,
        int button
    ) {
        if (
            mouseX < x ||
            mouseY < y ||
            mouseX >= x + visibleWidth ||
            mouseY >= y + visibleHeight
        ) {
            return false;
        }

        // Simplify grid position calculation to use consistent spacing
        int relX = mouseX - x + scrollX;
        int relY = mouseY - y + scrollY;

        // Simple division for cell index
        int gridX = relX / (CELL_SIZE + GRID_BORDER);
        int gridY = relY / (CELL_SIZE + GRID_BORDER);

        if (
            gridX < 0 ||
            gridY < 0 ||
            gridX >= grid.getCols() ||
            gridY >= grid.getRows()
        ) {
            return false;
        }

        if (button == 0) {
            // Left click
            // Check if we have an item at this position
            InventoryItem clickedItem = getItemAt(gridY, gridX);

            if (clickedItem != null) {
                // Start dragging the item
                draggedItem = clickedItem;

                // Find the item's top-left position
                Optional<int[]> pos = findItemPosition(clickedItem);
                if (pos.isPresent()) {
                    int[] itemPos = pos.get();

                    // Calculate correct drag offset based on the actual top-left corner
                    // and the mouse position relative to that corner
                    int itemTopLeftX =
                        x + itemPos[1] * (CELL_SIZE + GRID_BORDER) - scrollX;
                    int itemTopLeftY =
                        y + itemPos[0] * (CELL_SIZE + GRID_BORDER) - scrollY;

                    // Set drag offset based on mouse position relative to item's top-left corner
                    dragOffsetX = mouseX - itemTopLeftX;
                    dragOffsetY = mouseY - itemTopLeftY;

                    // Remove the item from the grid during drag
                    grid.removeItem(clickedItem);
                }
                return true;
            }
        } else if (button == 1) {
            // Right click
            // No longer rotates items (use R key instead)
            InventoryItem clickedItem = getItemAt(gridY, gridX);
            if (clickedItem != null) {
                // We could add other functionality here in the future
                return true;
            }
        }

        return false;
    }

    /**
     * Handle mouse drag event
     *
     * @param x Left position of the inventory
     * @param y Top position of the inventory
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     */
    public void mouseMoved(int x, int y, int mouseX, int mouseY) {
        if (
            mouseX >= x &&
            mouseX < x + visibleWidth &&
            mouseY >= y &&
            mouseY < y + visibleHeight
        ) {
            // Simplified hover position calculation
            int relX = mouseX - x + scrollX;
            int relY = mouseY - y + scrollY;

            // Simple division for cell index
            hoveredCol = relX / (CELL_SIZE + GRID_BORDER);
            hoveredRow = relY / (CELL_SIZE + GRID_BORDER);

            if (
                hoveredCol < 0 ||
                hoveredRow < 0 ||
                hoveredCol >= grid.getCols() ||
                hoveredRow >= grid.getRows()
            ) {
                hoveredCol = -1;
                hoveredRow = -1;
            }
        } else {
            hoveredCol = -1;
            hoveredRow = -1;
        }
    }

    /**
     * Handle mouse released event
     *
     * @param x Left position of the inventory
     * @param y Top position of the inventory
     * @param mouseX Mouse X position
     * @param mouseY Mouse Y position
     */
    public boolean mouseReleased(int x, int y, int mouseX, int mouseY) {
        if (draggedItem != null) {
            // Try to place the item at the hovered position
            if (hoveredRow >= 0 && hoveredCol >= 0) {
                // Offset the placement position based on where the user grabbed the item
                // to ensure it feels natural when placing
                int placeRow = hoveredRow;
                int placeCol = hoveredCol;

                // Adjust for dragging offset - ensure we place where the cursor is pointing
                // relative to where the user initially grabbed the item
                // Simple calculation for grid offsets
                int grabOffsetRow = dragOffsetY / (CELL_SIZE + GRID_BORDER);
                int grabOffsetCol = dragOffsetX / (CELL_SIZE + GRID_BORDER);

                // Adjust placement position by the grab offset
                placeRow = Math.max(0, placeRow - grabOffsetRow);
                placeCol = Math.max(0, placeCol - grabOffsetCol);

                // Make sure we don't place outside the grid
                placeRow = Math.min(
                    placeRow,
                    grid.getRows() - draggedItem.getHeight()
                );
                placeCol = Math.min(
                    placeCol,
                    grid.getCols() - draggedItem.getWidth()
                );

                if (grid.canPlaceItem(draggedItem, placeRow, placeCol)) {
                    grid.placeItem(draggedItem, placeRow, placeCol);
                } else {
                    // Try to find another valid position
                    tryPlaceItem(draggedItem);
                }
            } else {
                // Try to find any valid position
                tryPlaceItem(draggedItem);
            }

            draggedItem = null;
            return true;
        }
        return false;
    }

    /**
     * Try to place an item in the grid, searching for valid position
     */
    private boolean tryPlaceItem(InventoryItem item) {
        // First try original position if known
        Optional<int[]> originalPos = findItemPosition(item);
        if (originalPos.isPresent()) {
            int[] pos = originalPos.get();
            if (grid.canPlaceItem(item, pos[0], pos[1])) {
                grid.placeItem(item, pos[0], pos[1]);
                return true;
            }
        }

        // Otherwise try to find any valid position
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.canPlaceItem(item, r, c)) {
                    grid.placeItem(item, r, c);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Add a new item to the inventory
     *
     * @param item The item to add
     * @return True if the item was successfully placed
     */
    public boolean addItem(InventoryItem item) {
        if (!items.contains(item)) {
            items.add(item);
        }

        return tryPlaceItem(item);
    }

    /**
     * Remove an item from the inventory
     *
     * @param item The item to remove
     */
    public void removeItem(InventoryItem item) {
        grid.removeItem(item);
        items.remove(item);
    }

    /**
     * Create and add an item to the inventory
     *
     * @param stack The ItemStack
     * @param width Width in grid cells
     * @param height Height in grid cells
     * @param rotatable Whether the item can be rotated
     * @return The created item, or null if couldn't be placed
     */
    public InventoryItem createAndAddItem(
        ItemStack stack,
        int width,
        int height,
        boolean rotatable
    ) {
        InventoryItem item = new InventoryItem(stack, width, height, rotatable);
        if (addItem(item)) {
            return item;
        } else {
            items.remove(item);
            return null;
        }
    }

    /**
     * Find the grid position of an item
     *
     * @param item The item to find
     * @return The row and column of the top-left corner of the item, or empty if not found
     */
    public Optional<int[]> findItemPosition(InventoryItem item) {
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.getItemAt(r, c) == item) {
                    // Find the top-left corner
                    int minR = r;
                    int minC = c;

                    while (
                        minR > 0 && grid.getItemAt(minR - 1, c) == item
                    ) minR--;
                    while (
                        minC > 0 && grid.getItemAt(r, minC - 1) == item
                    ) minC--;

                    return Optional.of(new int[] { minR, minC });
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Get the item at a specific grid position
     *
     * @param row Grid row
     * @param col Grid column
     * @return The item at the position, or null if empty
     */
    public InventoryItem getItemAt(int row, int col) {
        if (
            row < 0 || col < 0 || row >= grid.getRows() || col >= grid.getCols()
        ) {
            return null;
        }
        return grid.getItemAt(row, col);
    }

    /**
     * Get all items in this inventory
     */
    public List<InventoryItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Set the visible area dimensions
     *
     * @param width Width in pixels
     * @param height Height in pixels
     */
    public void setVisibleArea(int width, int height) {
        // Use exact pixel dimensions
        this.visibleWidth = width;
        this.visibleHeight = height;
        updateScrollLimits();
    }

    /**
     * Resize the grid
     *
     * @param newRows New number of rows
     * @param newCols New number of columns
     * @param itemHandler Handler for items that don't fit in the new grid
     */
    public void resizeGrid(
        int newRows,
        int newCols,
        Consumer<InventoryItem> itemHandler
    ) {
        InventoryGrid newGrid = new InventoryGrid(newRows, newCols);

        // Try to transfer items to the new grid
        for (InventoryItem item : items) {
            Optional<int[]> pos = findItemPosition(item);
            if (pos.isPresent()) {
                int[] itemPos = pos.get();
                if (
                    itemPos[0] < newRows &&
                    itemPos[1] < newCols &&
                    itemPos[0] + item.getHeight() <= newRows &&
                    itemPos[1] + item.getWidth() <= newCols &&
                    newGrid.canPlaceItem(item, itemPos[0], itemPos[1])
                ) {
                    newGrid.placeItem(item, itemPos[0], itemPos[1]);
                } else {
                    // Item doesn't fit in new grid
                    if (itemHandler != null) {
                        itemHandler.accept(item);
                    }
                }
            }
        }

        // Update grid reference and scroll limits
        this.grid = newGrid;
        updateScrollLimits();
    }

    /**
     * Rotates the item currently being dragged.
     * Only works if the item is rotatable.
     *
     * @return true if an item was rotated, false otherwise
     */
    public boolean rotateDraggedItem() {
        // Check if we have a dragged item
        if (draggedItem != null) {
            // Check if the item is rotatable
            if (
                draggedItem.getStack() != null &&
                draggedItem.getStack() != ItemStack.EMPTY &&
                draggedItem.isRotatable()
            ) {
                // Calculate current dimensions in pixels
                int oldWidthPx =
                    draggedItem.getWidth() * (CELL_SIZE + GRID_BORDER);
                int oldHeightPx =
                    draggedItem.getHeight() * (CELL_SIZE + GRID_BORDER);

                // Remember the relative grab position (as percentage of item size)
                float relativeGrabX = (float) dragOffsetX / oldWidthPx;
                float relativeGrabY = (float) dragOffsetY / oldHeightPx;

                // Record whether item was rotated before this rotation
                boolean wasRotated = draggedItem.isRotated();

                // Rotate the item (this will swap dimensions if going from normal to rotated)
                draggedItem.rotate();

                // Get new dimensions in pixels
                int newWidthPx =
                    draggedItem.getWidth() * (CELL_SIZE + GRID_BORDER);
                int newHeightPx =
                    draggedItem.getHeight() * (CELL_SIZE + GRID_BORDER);

                // If dimensions actually swapped
                if (wasRotated != draggedItem.isRotated()) {
                    // When dimensions swap, we need to swap relative coordinates too
                    float temp = relativeGrabX;
                    relativeGrabX = relativeGrabY;
                    relativeGrabY = temp;
                }

                // Calculate new drag offsets using the new dimensions
                dragOffsetX = Math.round(relativeGrabX * newWidthPx);
                dragOffsetY = Math.round(relativeGrabY * newHeightPx);

                return true;
            }
        }
        return false;
    }

    /**
     * Rotates the item currently being hovered by the cursor.
     * Only works if the item is rotatable.
     *
     * @return true if an item was rotated, false otherwise
     */
    public boolean rotateHoveredItem() {
        // Check if we have valid hover coordinates
        if (hoveredRow >= 0 && hoveredCol >= 0) {
            InventoryItem item = getItemAt(hoveredRow, hoveredCol);
            if (item != null) {
                // Find the item's actual position (top-left corner)
                Optional<int[]> posOpt = findItemPosition(item);
                if (posOpt.isPresent()) {
                    // Check if the item is rotatable
                    if (
                        item.getStack() != null &&
                        item.getStack() != ItemStack.EMPTY
                    ) {
                        // Get the item's original position
                        int[] pos = posOpt.get();

                        // Remove the item from the grid first
                        grid.removeItem(item);

                        // Rotate the item
                        item.rotate();

                        // Try to place the item back at the same position
                        if (grid.canPlaceItem(item, pos[0], pos[1])) {
                            grid.placeItem(item, pos[0], pos[1]);
                            return true;
                        } else {
                            // If it can't be placed after rotation, rotate it back
                            item.rotate();
                            grid.placeItem(item, pos[0], pos[1]);
                        }
                    }
                }
            }
        }
        return false;
    }
}
