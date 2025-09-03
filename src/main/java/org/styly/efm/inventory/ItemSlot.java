package org.styly.efm.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.styly.efm.registries.ModItems;

public class ItemSlot implements Component, ItemContainer {

    int posX;
    int posY;
    int slotSize = 64;
    InventoryItem item = new InventoryItem(
            ModItems.NVG_WP.toStack(),
            2,
            2,
            false
    );

    public ItemSlot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            int offsetY
    ) {
        int halfSize = slotSize / 2;

        int x1 = posX - halfSize;
        int y1 = posY - halfSize - offsetY;
        int x2 = posX + halfSize;
        int y2 = posY + halfSize - offsetY;

        // Slot background (semi-transparent dark gray)
        int bgColor = 0x88000000;
        guiGraphics.fill(x1, y1, x2, y2, bgColor);

        // Render item inside slot
        if (!item.getItemStack().isEmpty()) {
            guiGraphics.pose().pushPose();
            float padding = 8f;
            // Padding around item inside slot

            float availableSize = slotSize - padding * 2;

            // Scale factor for item
            float scale = availableSize / 16.0f;
            guiGraphics.pose().translate(posX, posY - offsetY, 0);
            guiGraphics.pose().scale(scale, scale, 1.0f);

            guiGraphics.renderItem(item.getItemStack(), -8, -8);
            guiGraphics.renderItemDecorations(
                    Minecraft.getInstance().font,
                    item.getItemStack(),
                    -8,
                    -8
            );

            guiGraphics.pose().popPose();
        }

        // Slot border (solid white, 1px thick)
        int borderColor = 0xFFFFFFFF;
        guiGraphics.fill(x1, y1, x2, y1 + 1, borderColor); // top
        guiGraphics.fill(x1, y2 - 1, x2, y2, borderColor); // bottom
        guiGraphics.fill(x1, y1, x1 + 1, y2, borderColor); // left
        guiGraphics.fill(x2 - 1, y1, x2, y2, borderColor); // right

        // Hover highlight
        if (mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2) {
            int hoverColor = 0x44FFFFFF; // semi-transparent white
            guiGraphics.fill(x1, y1, x2, y2, hoverColor);
        }
    }

    public boolean over(double mouseX, double mouseY, int offsetY) {
        int halfSize = slotSize / 2;
        int x1 = posX - halfSize;
        int y1 = posY - halfSize - offsetY;
        int x2 = posX + halfSize;
        int y2 = posY + halfSize - offsetY;
        return mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2;
    }

    @Override
    public boolean handleClick(
            double mouseX,
            double mouseY,
            int offsetY,
            int button,
            DragContext ctx
    ) {
        if (
                over(mouseX, mouseY, offsetY) &&
                        button == GLFW.GLFW_MOUSE_BUTTON_LEFT &&
                        !this.item.getItemStack().isEmpty()
        ) {
            ctx.from = this;
            ctx.dragging = true;
            ctx.dragged = this.item;
            this.item = new InventoryItem(ItemStack.EMPTY, 0, 0, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleRelease(
            double mouseX,
            double mouseY,
            int offsetY,
            int button,
            DragContext ctx
    ) {
        if (
                button == GLFW.GLFW_MOUSE_BUTTON_LEFT &&
                        ctx.dragging &&
                        over(mouseX, mouseY, offsetY)
        ) {
            // Save the current dragged item before swapping
            InventoryItem draggedItem = ctx.dragged;

            // If there's already an item in the slot, try to return it to its original position
            if (!item.getItemStack().isEmpty()) {
                try {
                    ctx.from.returnItem(item, ctx.originContext);
                } catch (RuntimeException e) {
                    // If the item can't be returned, cancel the entire operation
                    return false;
                }
            }

            // Clear drag context
            ctx.dragging = false;
            ctx.from = null;
            ctx.originContext = null;

            // Place the dragged item in this slot
            item = draggedItem;
            ctx.dragged = new InventoryItem(ItemStack.EMPTY, 0, 0, false);
            return true;
        }
        return false;
    }

    @Override
    public void returnItem(InventoryItem item, Object contextData) {
        // In ItemSlot, we always accept the item since there's only one slot
        // If we already have an item, this would be a logic error in the calling code
        if (!this.item.getItemStack().isEmpty()) {
            throw new RuntimeException(
                    "ItemSlot already contains an item, cannot return another"
            );
        }
        this.item = item;
    }
}
