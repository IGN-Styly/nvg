package org.styly.efm.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.styly.efm.EFM;

/**
 * Simple player renderer based on FirstAid's approach.
 * Renders a player body using texture blitting only.
 */
public class Player2DRenderer {

    private static final ResourceLocation PLAYER_TEXTURE = EFM.id(
            "textures/gui/player_body.png"
    );
    private static final int SIZE = 32;
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 64;

    // Equipment items

    /**
     * Renders the player body using texture blitting.
     */
    public void render(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            int scale
    ) {
        // Center the player in the available space
        int centerX = (x + (width - PLAYER_WIDTH * scale) / 2) / scale;
        int centerY = (y + (height - PLAYER_HEIGHT * scale) / 2) / scale;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1f);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        // Draw body parts exactly as FirstAid does
        drawPart(guiGraphics, centerX + 8, centerY, 8, 0, 16, 16); // HEAD
        drawPart(guiGraphics, centerX + 8, centerY + 16, 8, 16, 16, 24); // BODY
        drawPart(guiGraphics, centerX, centerY + 16, 0, 16, 8, 24); // LEFT_ARM
        drawPart(guiGraphics, centerX + 24, centerY + 16, 24, 16, 8, 24); // RIGHT_ARM
        drawPart(guiGraphics, centerX + 8, centerY + 40, 8, 40, 8, 16); // LEFT_LEG
        drawPart(guiGraphics, centerX + 16, centerY + 40, 16, 40, 8, 16); // RIGHT_LEG
        drawPart(guiGraphics, centerX + 8, centerY + 56, 8, 56, 8, 8); // LEFT_FOOT
        drawPart(guiGraphics, centerX + 16, centerY + 56, 16, 56, 8, 8); // RIGHT_FOOT
        guiGraphics.pose().popPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    /**
     * Draws a single body part using texture coordinates.
     * Similar to FirstAid's drawPart method.
     */
    private void drawPart(
            GuiGraphics guiGraphics,
            int screenX,
            int screenY,
            int texX,
            int texY,
            int width,
            int height
    ) {
        // Match FirstAid's implementation exactly
        int rawTexX = texX;
        // We only have one state (healthy) so no need to multiply by state
        guiGraphics.blit(
                PLAYER_TEXTURE,
                screenX,
                screenY,
                texX,
                texY,
                width,
                height
        );
    }

    /**
     * Gets the recommended width for the player area.
     */
    public int getRecommendedWidth() {
        return 50;
    }

    /**
     * Gets the recommended height for the player area.
     */
    public int getRecommendedHeight() {
        return 80;
    }


}
