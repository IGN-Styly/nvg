package org.styly.efm.inventory;

import net.minecraft.client.gui.GuiGraphics;

public interface Component {
    void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY
    );
    boolean over(double mouseX, double mouseY);
}
