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


    DragContext context = new DragContext();
    ArrayList<org.styly.efm.inventory.Component> components=new ArrayList<>();
    public EFMInventoryScreen() {
        super(Component.literal(""));
        ItemSlot head = new ItemSlot(32, 32);
        ItemSlot chest = new ItemSlot(32, 128);
        ScrollableArea area = new ScrollableArea(new ItemSlot(128, 32));
        chest.item.setItemStack(Items.DIAMOND_SWORD.getDefaultInstance());
        components.add(head);
        components.add(chest);
        components.add(area);
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
            component.handleClick(mouseX,mouseY,button,context);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean ret =false;
        for(org.styly.efm.inventory.Component component:components){
            if(component.handleRelease(mouseX,mouseY,button,context))ret=true;
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
        return false;
    }

}
