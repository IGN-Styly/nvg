package org.styly.efm.player;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Represents an equipment slot that can hold specific types of items.
 * Provides rendering and interaction capabilities for player equipment.
 */
public class EquipmentSlot {

    /**
     * Different types of equipment slots available.
     */
    public enum SlotType {
        PRIMARY_WEAPON("Primary", 48, 16),
        SECONDARY_WEAPON("Secondary", 32, 16),
        HOLSTER("Holster", 16, 16),
        BODY_ARMOR("Body Armor", 32, 32),
        HEADWEAR("Headwear", 16, 16),
        TACTICAL_RIG_POCKET("Rig Pocket", 16, 16),
        BACKPACK("Backpack", 32, 32);

        private final String displayName;
        private final int defaultWidth;
        private final int defaultHeight;

        SlotType(String displayName, int defaultWidth, int defaultHeight) {
            this.displayName = displayName;
            this.defaultWidth = defaultWidth;
            this.defaultHeight = defaultHeight;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getDefaultWidth() {
            return defaultWidth;
        }

        public int getDefaultHeight() {
            return defaultHeight;
        }
    }

    // Slot properties
    private final SlotType type;
    private ItemStack item;
    private int x, y, width, height;
    private boolean isHovered;
    private boolean isVisible;

    // Visual constants
    private static final int SLOT_BORDER_COLOR = 0xFF555555;
    private static final int SLOT_BACKGROUND_COLOR = 0xFF222222;
    private static final int SLOT_HOVER_COLOR = 0xFF777777;
    private static final int SLOT_OCCUPIED_COLOR = 0xFF333333;

    /**
     * Creates a new equipment slot.
     *
     * @param type The type of equipment this slot accepts
     * @param x Screen x position
     * @param y Screen y position
     */
    public EquipmentSlot(SlotType type, int x, int y) {
        this.type = type;
        this.item = ItemStack.EMPTY;
        this.x = x;
        this.y = y;
        this.width = type.getDefaultWidth();
        this.height = type.getDefaultHeight();
        this.isHovered = false;
        this.isVisible = true;
    }

    /**
     * Creates a new equipment slot with custom size.
     */
    public EquipmentSlot(SlotType type, int x, int y, int width, int height) {
        this.type = type;
        this.item = ItemStack.EMPTY;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isHovered = false;
        this.isVisible = true;
    }

    /**
     * Renders the equipment slot.
     */
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!isVisible) return;

        // Update hover state
        isHovered =
            mouseX >= x &&
            mouseX < x + width &&
            mouseY >= y &&
            mouseY < y + height;

        // Choose background color based on state
        int backgroundColor = SLOT_BACKGROUND_COLOR;
        if (!item.isEmpty()) {
            backgroundColor = SLOT_OCCUPIED_COLOR;
        }
        if (isHovered) {
            backgroundColor = SLOT_HOVER_COLOR;
        }

        // Draw slot background
        guiGraphics.fill(x, y, x + width, y + height, backgroundColor);

        // Draw slot border
        guiGraphics.fill(x - 1, y - 1, x + width + 1, y, SLOT_BORDER_COLOR); // Top
        guiGraphics.fill(
            x - 1,
            y + height,
            x + width + 1,
            y + height + 1,
            SLOT_BORDER_COLOR
        ); // Bottom
        guiGraphics.fill(x - 1, y, x, y + height, SLOT_BORDER_COLOR); // Left
        guiGraphics.fill(
            x + width,
            y,
            x + width + 1,
            y + height,
            SLOT_BORDER_COLOR
        ); // Right

        // Render item if present
        if (!item.isEmpty()) {
            // Center the item in the slot
            int itemX = x + (width - 16) / 2;
            int itemY = y + (height - 16) / 2;

            guiGraphics.renderItem(item, itemX, itemY);
            guiGraphics.renderItemDecorations(
                net.minecraft.client.Minecraft.getInstance().font,
                item,
                itemX,
                itemY
            );
        }

        // Draw slot type indicator for empty slots
        if (item.isEmpty() && isHovered) {
            // Draw a subtle indicator showing what type of item can go here
            String indicator = getSlotIndicator();
            if (!indicator.isEmpty()) {
                int textWidth =
                    net.minecraft.client.Minecraft.getInstance().font.width(
                        indicator
                    );
                int textX = x + (width - textWidth) / 2;
                int textY = y + height + 2;

                guiGraphics.drawString(
                    net.minecraft.client.Minecraft.getInstance().font,
                    indicator,
                    textX,
                    textY,
                    0xFF888888
                );
            }
        }
    }

    /**
     * Gets a visual indicator for the slot type.
     */
    private String getSlotIndicator() {
        switch (type) {
            case PRIMARY_WEAPON:
                return "1°";
            case SECONDARY_WEAPON:
                return "2°";
            case HOLSTER:
                return "H";
            case BODY_ARMOR:
                return "A";
            case HEADWEAR:
                return "⛑";
            case TACTICAL_RIG_POCKET:
                return "P";
            case BACKPACK:
                return "🎒";
            default:
                return "";
        }
    }

    /**
     * Checks if the given item is compatible with this slot.
     * This is where you'd implement your compatibility logic.
     */
    public boolean isItemCompatible(ItemStack itemStack) {
        if (itemStack.isEmpty()) return true;

        // Improved compatibility check with better item categorization
        switch (type) {
            case PRIMARY_WEAPON:
                return isPrimaryWeapon(itemStack);
            case SECONDARY_WEAPON:
                return isSecondaryWeapon(itemStack);
            case HOLSTER:
                return isHolsterWeapon(itemStack);
            case BODY_ARMOR:
                return isBodyArmor(itemStack);
            case HEADWEAR:
                return isHeadwear(itemStack);
            case TACTICAL_RIG_POCKET:
                return isSmallItem(itemStack);
            case BACKPACK:
                return isBackpack(itemStack);
            default:
                return true;
        }
    }

    // Helper methods for item type checking
    private boolean isPrimaryWeapon(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getItem() == Items.DIAMOND_SWORD ||
            item.getItem() == Items.IRON_SWORD ||
            item.getItem() == Items.GOLDEN_SWORD ||
            item.getItem() == Items.STONE_SWORD ||
            item.getItem() == Items.WOODEN_SWORD ||
            item.getItem() == Items.NETHERITE_SWORD ||
            item.getItem() == Items.BOW ||
            item.getItem() == Items.CROSSBOW ||
            itemName.contains("rifle") ||
            itemName.contains("assault") ||
            itemName.contains("sniper")
        );
    }

    private boolean isSecondaryWeapon(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getItem() == Items.BOW ||
            item.getItem() == Items.CROSSBOW ||
            item.getItem() == Items.DIAMOND_SWORD ||
            item.getItem() == Items.IRON_SWORD ||
            itemName.contains("pistol") ||
            itemName.contains("smg") ||
            itemName.contains("carbine")
        );
    }

    private boolean isHolsterWeapon(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            itemName.contains("pistol") ||
            itemName.contains("revolver") ||
            item.getItem() == Items.FLINT_AND_STEEL
        ); // Example small weapon
    }

    private boolean isBodyArmor(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getItem() == Items.IRON_CHESTPLATE ||
            item.getItem() == Items.DIAMOND_CHESTPLATE ||
            item.getItem() == Items.LEATHER_CHESTPLATE ||
            item.getItem() == Items.GOLDEN_CHESTPLATE ||
            item.getItem() == Items.CHAINMAIL_CHESTPLATE ||
            item.getItem() == Items.NETHERITE_CHESTPLATE ||
            itemName.contains("vest") ||
            itemName.contains("armor") ||
            itemName.contains("chestplate")
        );
    }

    private boolean isHeadwear(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getItem() == Items.IRON_HELMET ||
            item.getItem() == Items.DIAMOND_HELMET ||
            item.getItem() == Items.LEATHER_HELMET ||
            item.getItem() == Items.GOLDEN_HELMET ||
            item.getItem() == Items.CHAINMAIL_HELMET ||
            item.getItem() == Items.NETHERITE_HELMET ||
            item.getItem() == Items.TURTLE_HELMET ||
            itemName.contains("helmet") ||
            itemName.contains("hat") ||
            itemName.contains("cap")
        );
    }

    private boolean isSmallItem(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getMaxStackSize() > 1 ||
            item.getItem() == Items.COMPASS ||
            item.getItem() == Items.CLOCK ||
            item.getItem() == Items.MAP ||
            item.getItem() == Items.SPYGLASS ||
            itemName.contains("grenade") ||
            itemName.contains("magazine") ||
            itemName.contains("ammo") ||
            itemName.contains("bandage") ||
            itemName.contains("medkit")
        );
    }

    private boolean isBackpack(ItemStack item) {
        String itemName = item.getItem().toString().toLowerCase();
        return (
            item.getItem() == Items.BUNDLE ||
            item.getItem() == Items.SHULKER_BOX ||
            itemName.contains("backpack") ||
            itemName.contains("bag") ||
            itemName.contains("satchel")
        );
    }

    /**
     * Attempts to place an item in this slot.
     * Returns the item that was previously in the slot, or empty if placement failed.
     */
    public ItemStack tryPlaceItem(ItemStack newItem) {
        if (!isItemCompatible(newItem)) {
            return newItem; // Return the item unchanged if incompatible
        }

        ItemStack oldItem = this.item.copy();
        this.item = newItem.copy();
        return oldItem;
    }

    /**
     * Removes and returns the item from this slot.
     */
    public ItemStack removeItem() {
        ItemStack removed = this.item.copy();
        this.item = ItemStack.EMPTY;
        return removed;
    }

    /**
     * Checks if the mouse position is over this slot.
     */
    public boolean isMouseOver(int mouseX, int mouseY) {
        return (
            mouseX >= x &&
            mouseX < x + width &&
            mouseY >= y &&
            mouseY < y + height
        );
    }

    // Getters and setters
    public SlotType getType() {
        return type;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item != null ? item : ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return item.isEmpty();
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
