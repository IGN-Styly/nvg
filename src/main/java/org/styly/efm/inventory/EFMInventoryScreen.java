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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.styly.efm.player.EquipmentSlot;
import org.styly.efm.player.Player2DRenderer;

/**
 * Screen for displaying and interacting with an EFM inventory.
 */
@OnlyIn(Dist.CLIENT)
public class EFMInventoryScreen extends Screen {

    public EFMInventoryScreen() {
        super(Component.literal(""));
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        new Player2DRenderer().render(guiGraphics,32,guiGraphics.guiHeight()/2-128,128,256,4);

    }
}
