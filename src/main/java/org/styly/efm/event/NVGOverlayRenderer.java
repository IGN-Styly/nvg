package org.styly.efm.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.styly.efm.EFM;
import org.styly.efm.registries.DataCompReg;
import org.styly.efm.registries.ModItems;

import static org.styly.efm.EFM.id;

@EventBusSubscriber(modid = EFM.MODID, value = Dist.CLIENT)
public class NVGOverlayRenderer {
    private static final ResourceLocation OVERLAY = id("textures/misc/nvgov.png");
    private static ShaderInstance shader; // Your custom shader instance

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack nvg = minecraft.player.getItemBySlot(EquipmentSlot.HEAD);

        if (nvg.has(DataCompReg.NVG_TOGGLE) && nvg.get(DataCompReg.NVG_TOGGLE).toggle()) {
            renderHelmetOverlay(event.getGuiGraphics());
        }
    }

    private static void renderHelmetOverlay(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();

        // Bind the overlay texture
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OVERLAY);

        // Set blending to allow transparency in the overlay
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Get the screen dimensions
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // Draw the overlay texture
        guiGraphics.blit(OVERLAY, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

        // Disable blending after rendering
        RenderSystem.disableBlend();
    }
}
