package org.styly.efm.inventory;

import net.minecraft.client.gui.GuiGraphics;

public interface Component {
    void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            int offsetY
    );
    boolean over(double mouseX, double mouseY);
    boolean handleClick(double mouseX,double mouseY,int button,DragContext ctx);
    boolean handleRelease(double mouseX,double mouseY,int button,DragContext ctx);
}
