package org.styly.efm.inventory;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Arrays;

public class ScrollableArea implements Component{
    ArrayList<Component> components=new ArrayList<>();
    public ScrollableArea(Component... component){
        this.components.addAll(Arrays.asList(component));

    }
    int yOffset = 0;
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY,int offsetY) {
        for(Component component : components){
            component.render(guiGraphics,mouseX,mouseY,0);
        }
    }

    @Override
    public boolean over(double mouseX, double mouseY) {
        for(Component component : components){
            component.over(mouseX,mouseY);
        }
        return false;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button, DragContext ctx) {
        for (Component component : components){
            component.handleClick(mouseX,mouseY,button,ctx);
        }
        return false;
    }

    @Override
    public boolean handleRelease(double mouseX, double mouseY, int button, DragContext ctx) {
        boolean ret = false;
        for (Component component : components) {

            if (component.handleRelease(mouseX, mouseY, button, ctx)) ret= true;
        }
        return ret;
    }
}
