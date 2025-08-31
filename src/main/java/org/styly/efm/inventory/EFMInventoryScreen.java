package org.styly.efm.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.lwjgl.glfw.GLFW;
import org.styly.efm.player.EquipmentSlot;
import org.styly.efm.player.Player2DRenderer;

/**
 * Screen for displaying and interacting with an EFM inventory.
 */
@OnlyIn(Dist.CLIENT)
public class EFMInventoryScreen extends Screen {

    ItemSlot head = new ItemSlot(32, 32);
    ItemSlot chest = new ItemSlot(32, 128);
    InventoryItem dragged;
    ItemSlot from;
    boolean dragging=false;

    public EFMInventoryScreen() {
        super(Component.literal(""));
        chest.item.setItemStack(Items.DIAMOND_SWORD.getDefaultInstance());
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
        if(dragging && dragged!=null){
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(2,2,1);
            guiGraphics.renderFakeItem(dragged.getStack(),mouseX+8/2,mouseY+8/2);
            guiGraphics.pose().popPose();
        }
        head.render(guiGraphics, mouseX, mouseY);
        chest.render(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
           if(head.over(mouseX,mouseY)&&!dragging){
               dragged = head.item;
               head.item=new InventoryItem(ItemStack.EMPTY,0,0,false);
                from = head;
               dragging=true;
           } else if(chest.over(mouseX,mouseY)&&!dragging){

                    dragged = chest.item;
                    chest.item=new InventoryItem(ItemStack.EMPTY,0,0,false);
                    from = chest;
                    dragging=true;

            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && dragging) {
            if(head.over(mouseX,mouseY)){
                if(!head.item.getItemStack().isEmpty()){
                    from.item=head.item;
                }
                dragging=false;
                head.item=dragged;
                dragged=new InventoryItem(ItemStack.EMPTY,0,0,false);
            } else if (chest.over(mouseX,mouseY)) {
                if(!chest.item.getItemStack().isEmpty()){
                    from.item=chest.item;
                }
                dragging=false;
                chest.item=dragged;
                dragged=new InventoryItem(ItemStack.EMPTY,0,0,false);
            } else {
                dragging=false;
                from.item=dragged;
                dragged=new InventoryItem(ItemStack.EMPTY,0,0,false);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
