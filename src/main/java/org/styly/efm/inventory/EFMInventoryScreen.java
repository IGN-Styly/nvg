package org.styly.efm.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Screen for displaying and interacting with an EFM inventory.
 */
@OnlyIn(Dist.CLIENT)
public class EFMInventoryScreen extends Screen {

    // The inventory system
    private final EFMInventory inventory;

    // Screen dimensions
    private int backgroundWidth = 320;
    private int backgroundHeight = 240;

    // Position of the inventory grid within the screen
    private int inventoryX;
    private int inventoryY;

    /**
     * Creates a new EFMInventoryScreen with default settings.
     */
    public EFMInventoryScreen() {
        super(Component.translatable("screen.nvg.efm_inventory"));
        // Create inventory with default size (can be customized)
        this.inventory = new EFMInventory(
            10,
            9,
            backgroundWidth - 20,
            backgroundHeight - 40
        );

        // Add some example items (you'd replace this with your actual items)
        addExampleItems();
    }

    /**
     * Creates a new EFMInventoryScreen with custom inventory.
     *
     * @param inventory The pre-configured inventory to use
     */
    public EFMInventoryScreen(EFMInventory inventory) {
        super(Component.translatable("screen.nvg.efm_inventory"));
        this.inventory = inventory;
    }

    /**
     * Adds some example items to the inventory.
     * Replace with your actual item loading code.
     */
    private void addExampleItems() {
        // 2x2 item example - shields should not be rotatable
        inventory.createAndAddItem(new ItemStack(Items.SHIELD), 2, 2, false);

        // 1x3 item example
        inventory.createAndAddItem(
            new ItemStack(Items.DIAMOND_SWORD),
            1,
            3,
            true
        );

        // 3x2 item example
        inventory.createAndAddItem(new ItemStack(Items.BOW), 3, 2, true);

        // 1x1 items
        inventory.createAndAddItem(new ItemStack(Items.APPLE, 5), 1, 1, false);
        inventory.createAndAddItem(new ItemStack(Items.COMPASS), 1, 1, false);
    }

    @Override
    protected void init() {
        super.init();

        // Center the inventory on screen
        inventoryX = (width - backgroundWidth) / 2;
        inventoryY = (height - backgroundHeight) / 2;
    }

    @Override
    public void render(
        GuiGraphics guiGraphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        // IMPORTANT: Skip the renderBackground completely to avoid blur effects

        // Set up rendering state
        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();

        // Draw solid background without blur
        guiGraphics.fill(0, 0, this.width, this.height, 0xC0101010);

        // Draw inventory panel background
        guiGraphics.fill(
            inventoryX,
            inventoryY,
            inventoryX + backgroundWidth,
            inventoryY + backgroundHeight,
            0xFF222222
        );

        // Draw screen title
        guiGraphics.drawString(
            font,
            this.title,
            inventoryX + 10,
            inventoryY + 10,
            0xFFFFFF
        );

        // Render inventory grid
        inventory.render(
            guiGraphics,
            inventoryX + 10,
            inventoryY + 30,
            mouseX,
            mouseY
        );

        // Reset rendering state
        com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();

        // Call parent render method for buttons, etc.
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Forward mouse click to inventory
        if (
            inventory.mouseClicked(
                inventoryX + 10,
                inventoryY + 30,
                (int) mouseX,
                (int) mouseY,
                button
            )
        ) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Forward mouse release to inventory
        if (
            inventory.mouseReleased(
                inventoryX + 10,
                inventoryY + 30,
                (int) mouseX,
                (int) mouseY
            )
        ) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(
        double mouseX,
        double mouseY,
        int button,
        double dragX,
        double dragY
    ) {
        // Only handle when not handled by vanilla
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double horizontalAmount,
        double verticalAmount
    ) {
        // Use integer precise coordinates to avoid blurring from floating point positions
        int mx = (int) Math.round(mouseX);
        int my = (int) Math.round(mouseY);

        // Forward mouse scroll to inventory
        inventory.mouseScroll(
            inventoryX + 10,
            inventoryY + 30,
            mx,
            my,
            verticalAmount
        );
        return super.mouseScrolled(
            mouseX,
            mouseY,
            horizontalAmount,
            verticalAmount
        );
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        // Forward mouse move to inventory
        inventory.mouseMoved(
            inventoryX + 10,
            inventoryY + 30,
            (int) mouseX,
            (int) mouseY
        );
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        // Don't pause the game when this screen is open (like other inventory screens)
        return false;
    }

    public void renderBackground(GuiGraphics guiGraphics) {
        // Direct rendering without any blur effect
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Use integer coordinates for pixel-perfect rendering
        guiGraphics.fill(0, 0, this.width, this.height, 0xC0101010);
    }

    @Override
    public void renderBackground(
        GuiGraphics guiGraphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        // Skip parent implementation which applies blur
        this.renderBackground(guiGraphics);
    }

    /**
     * Opens this inventory screen.
     * Call this from client side only.
     */
    public static void open() {
        // Get Minecraft instance first
        net.minecraft.client.Minecraft mc =
            net.minecraft.client.Minecraft.getInstance();

        // Clear any blur effects from previous screens before setting new one
        mc.execute(() -> {
            // Set our screen directly, which will not use blurred backgrounds
            mc.setScreen(new EFMInventoryScreen());
        });
    }

    /**
     * Opens an inventory screen with a specific inventory.
     * Call this from client side only.
     *
     * @param inventory The inventory to display
     */
    public static void open(EFMInventory inventory) {
        // Get Minecraft instance first
        net.minecraft.client.Minecraft mc =
            net.minecraft.client.Minecraft.getInstance();

        // Clear any blur effects from previous screens before setting new one
        mc.execute(() -> {
            // Set our screen directly, which will not use blurred backgrounds
            mc.setScreen(new EFMInventoryScreen(inventory));
        });
    }
}
