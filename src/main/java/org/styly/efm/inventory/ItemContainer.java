package org.styly.efm.inventory;

public interface ItemContainer {
    /**
     * Returns an item to this container.
     *
     * @param item        The item to return
     * @param contextData Context data for the return operation, typically the original position
     * @throws RuntimeException if the item cannot be returned to its original position
     */
    void returnItem(InventoryItem item, Object contextData);
}
