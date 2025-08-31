package org.styly.efm.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.styly.efm.registries.ModItems;

public class ItemSlot implements Component{
    int posX;
    int posY;
    int slotSize = 64;
    InventoryItem item = new InventoryItem(
        ModItems.NVG_WP.toStack(),
        2,
        2,
        false
    );

    public ItemSlot(int posX,int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void render(
        GuiGraphics guiGraphics,
        int mouseX,
        int mouseY
    ) {
        int halfSize = slotSize / 2;

        int x1 = posX - halfSize;
        int y1 = posY - halfSize;
        int x2 = posX + halfSize;
        int y2 = posY + halfSize;

        // Slot background (semi-transparent dark gray)
        int bgColor = 0x88000000;
        guiGraphics.fill(x1, y1, x2, y2, bgColor);

        // Render item inside slot
        if (!item.getItemStack().isEmpty()) {
            guiGraphics.pose().pushPose();

            // Padding around item inside slot
            float padding = 4f;
            float availableSize = slotSize - padding * 2;

            // Scale factor for item
            float scale = availableSize / 16.0f;
            guiGraphics.pose().scale(scale, scale, 1.0f);

            // Center the item
            int scaledX = (int) ((x1 + padding) / scale);
            int scaledY = (int) ((y1 + padding) / scale);

            guiGraphics.renderItem(item.getItemStack(), scaledX, scaledY);
            guiGraphics.renderItemDecorations(
                Minecraft.getInstance().font,
                item.getItemStack(),
                scaledX,
                scaledY
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

    public boolean over(double mouseX,double mouseY) {
        int halfSize = slotSize / 2;

        int x1 = posX - halfSize;
        int y1 = posY - halfSize;
        int x2 = posX + halfSize;
        int y2 = posY + halfSize;
        if (mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2) {
            return true;
        }
        return false;
    }
}
