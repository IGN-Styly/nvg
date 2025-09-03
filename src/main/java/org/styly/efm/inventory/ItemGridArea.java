package org.styly.efm.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.styly.efm.EFM;

import java.util.HashMap;
import java.util.Map;

public class ItemGridArea implements Component,ItemContainer{
    InventoryGrid container = new InventoryGrid(7,6);
    int x;
    int y;
    int SIZE=24;
    public ItemGridArea(int x,int y){
        InventoryItem demo = new InventoryItem(Items.DIAMOND_SWORD.getDefaultInstance(),1,3,true);
        InventoryItem demo2 = new InventoryItem(Items.DIAMOND_HELMET.getDefaultInstance(),3,3,true);
        container.placeItem(demo,0,0);
        container.placeItem(demo2,0,1);
        this.x=x;
        this.y=y;
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, int offsetY) {
        HashMap<InventoryItem, Vector2d> metadata=new HashMap<>();
        for(int n =0; n<container.getRows();n++){
            for(int k =0;k<container.getCols();k++){
                if(!metadata.containsKey(container.getItemAt(n,k))&&container.getItemAt(n,k)!=null){
                    metadata.put(container.getItemAt(n,k), new Vector2d(k,n));
                }
            }
        }
        for(int n =0; n<container.getRows();n++){
            for(int k =0;k<container.getCols();k++){
                int bgColor = 0x88000000;
                int borderColor = 0xFFFFFFFF;
                int x1 = x+n*SIZE ;
                int y1 = y+k*SIZE-offsetY;
                int x2 = x+(n+1)*SIZE;
                int y2 = y + (k+1)*SIZE-offsetY;
                guiGraphics.fill(x1,y1,x2,y2,bgColor);

                guiGraphics.fill(x1, y1, x2, y1 + 1, borderColor); // top
                guiGraphics.fill(x1, y2 - 1, x2, y2, borderColor); // bottom
                guiGraphics.fill(x1, y1, x1 + 1, y2, borderColor); // left
                guiGraphics.fill(x2 - 1, y1, x2, y2, borderColor); // right
            }
        }

        for(Map.Entry<InventoryItem,Vector2d> k :metadata.entrySet()){
            int hoverColor = 0x44FFFFFF;
            float padding = 4f;
            float availableSize = SIZE*Math.max(Math.min(k.getKey().getHeight(),k.getKey().getWidth()),1)-(padding * 2);

            // Scale factor for item
            float scale = availableSize / 16.0f;
            Vector2d cords = k.getValue();
            int x1 = (int) (x+cords.x*SIZE);
            int y1 = (int) (y+cords.y*SIZE-offsetY);
            int x2 = (int) (x+(cords.x+k.getKey().getWidth())*SIZE);
            int y2 = (int) (y + (cords.y+k.getKey().getHeight())*SIZE-offsetY);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x1+ (float) (x2 - x1) /2,y1+ (float) (y2 - y1) /2,0);
            guiGraphics.pose().scale(scale, scale, 1.0f);
            guiGraphics.renderFakeItem(k.getKey().getStack(), -8, -8);
            guiGraphics.pose().popPose();
            if (mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2) {
            guiGraphics.fill(x1,y1,x2,y2,hoverColor);}
        }
    }

    @Override
    public boolean over(double mouseX,double mouseY,int offsetY) {

        int x1 = x ;
        int y1 = y -offsetY;
        int x2 = x + SIZE* container.getCols();
        int y2 = y + SIZE*container.getRows()-offsetY;
        return mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2;
    }
    public Vector2i cursorToGridPos(double mouseX, double mouseY){
        double localX = mouseX-x;
        double localY=mouseY-y;
        Vector2i pos = new Vector2i((int)localX/SIZE,(int)localY/SIZE);
        return pos;
    }
    @Override
    public boolean handleClick(double mouseX, double mouseY, int offsetY, int button, DragContext ctx) {
        Vector2i gridpos = cursorToGridPos(mouseX,mouseY);
        if(over(mouseX,mouseY,offsetY)&& button== GLFW.GLFW_MOUSE_BUTTON_LEFT&&this.container.getItemAt(gridpos.y,gridpos.x)!=null){
            ctx.from=this;
            ctx.originContext = gridpos;
            ctx.dragging=true;
            ctx.dragged=this.container.getItemAt(gridpos.y,gridpos.x);
            container.removeItem(this.container.getItemAt(gridpos.y,gridpos.x));
            return true;
        }
        return false;
    }

    @Override
    public boolean handleRelease(double mouseX, double mouseY,int offsetY, int button, DragContext ctx) {
        Vector2i gridpos = cursorToGridPos(mouseX,mouseY);
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && ctx.dragging&&over(mouseX,mouseY,offsetY)) {

            if(this.container.getItemAt(gridpos.y,gridpos.x)!=null){
                ctx.from.returnItem(this.container.getItemAt(gridpos.y,gridpos.x),ctx.originContext);
            }
            ctx.dragging=false;
            ctx.originContext=null;
            ctx.from=null;
            container.placeItem(ctx.dragged,gridpos.y,gridpos.x);
            ctx.dragged=new InventoryItem(ItemStack.EMPTY,0,0,false);
            return true;
        }
        return false;
    }

    @Override
    public void returnItem(InventoryItem item, Object contextData) {
        Vector2i ctx = (Vector2i) contextData;
        this.container.placeItem(item,ctx.y,ctx.x);
    }
}
