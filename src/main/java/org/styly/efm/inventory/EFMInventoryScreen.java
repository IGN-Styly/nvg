package org.styly.efm.inventory;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.styly.efm.player.Player2DRenderer;

import static org.styly.efm.EFM.LOGGER;

/**
 * Screen for displaying and interacting with an EFM inventory.
 */
@OnlyIn(Dist.CLIENT)
public class EFMInventoryScreen extends Screen {


    DragContext context = new DragContext();
    ArrayList<org.styly.efm.inventory.Component> components=new ArrayList<>();
    public EFMInventoryScreen() {
        super(Component.literal(""));
        ItemSlot head = new ItemSlot(32, 32);
        ItemSlot chest = new ItemSlot(32, 128);
        ItemGridArea grid = new ItemGridArea(128,128);
        ScrollableComponentArea area = new ScrollableComponentArea(new ItemSlot(128, 32));
        chest.item.setItemStack(Items.DIAMOND_SWORD.getDefaultInstance());
        components.add(head);
        components.add(chest);
        components.add(area);
        components.add(grid);
    }

    @Override
    public void render(
        GuiGraphics guiGraphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        new Player2DRenderer().render(
            guiGraphics,
            32,
            guiGraphics.guiHeight() / 2 - 192,
            192,
            384,
            6
        );
        if(context.dragging && context.dragged!=null){
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(2,2,1);
            guiGraphics.renderFakeItem(context.dragged.getStack(),mouseX/2-8,mouseY/2-8);
            guiGraphics.pose().popPose();
        }
        for(org.styly.efm.inventory.Component component:components){
            component.render(guiGraphics,mouseX,mouseY,0);
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(org.styly.efm.inventory.Component component:components){
            component.handleClick(mouseX,mouseY,0,button,context);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean ret =false;
        for(org.styly.efm.inventory.Component component:components){
            if(component.handleRelease(mouseX,mouseY,0,button,context))ret=true;
        }
        if(context.dragging&&!ret){
            context.from.item=context.dragged;
            context.dragging=false;
            context.dragged=null;
            context.from=null;
        }
        if (ret) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean ret = false;

        for(org.styly.efm.inventory.Component component :components){
            if (component instanceof ScrollableComponent comp) {
                if(comp.mouseScrolled(mouseX,mouseY,scrollX,scrollY,0))ret=true;
            }
        }
    return ret;
    }

}
