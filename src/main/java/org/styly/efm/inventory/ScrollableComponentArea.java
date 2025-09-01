package org.styly.efm.inventory;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Arrays;

import static org.styly.efm.EFM.LOGGER;

public class ScrollableComponentArea implements Component, ScrollableComponent {
    int offsetY=0;
    int lowerLim=-1080;
    int upperLim=0;
    ArrayList<Component> components=new ArrayList<>();
    public ScrollableComponentArea(Component... component){
        this.components.addAll(Arrays.asList(component));

    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY,int offsetY) {
        for(Component component : components){
            component.render(guiGraphics,mouseX,mouseY,offsetY+this.offsetY);
        }

     }

    @Override
    public boolean over(double mouseX, double mouseY,int offsetY) {
        boolean ret =false;
        for(Component component : components){
            if(component.over(mouseX,mouseY,offsetY+this.offsetY)) {ret=true;}
        }
        return ret;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY,int offsetY, int button, DragContext ctx) {
        boolean ret =false;
        for (Component component : components){
            if(component.handleClick(mouseX,mouseY,offsetY+this.offsetY,button,ctx))ret=true;
        }
        return ret;
    }

    @Override
    public boolean handleRelease(double mouseX, double mouseY,int offsetY, int button, DragContext ctx) {
        boolean ret = false;
        for (Component component : components) {

            if (component.handleRelease(mouseX, mouseY,offsetY+this.offsetY, button, ctx)) ret= true;
        }
        return ret;
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY,int offsetY) {
        if(over(mouseX,mouseY,offsetY)){
            this.offsetY=Math.clamp((int) (scrollY*4)+this.offsetY,lowerLim,upperLim);
        return true;}
        return false;
    }
}
