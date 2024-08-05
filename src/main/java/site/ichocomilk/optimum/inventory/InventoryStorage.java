package site.ichocomilk.optimum.inventory;

import site.ichocomilk.optimum.inventory.data.CacheableInventory;
import site.ichocomilk.optimum.inventory.item.DropClickHandler;

public final class InventoryStorage {

    private static InventoryStorage storage;

    private final CacheableInventory main;
    private final DropClickHandler[] dropInventoryClick;

    InventoryStorage(CacheableInventory main, DropClickHandler[] dropInventoryClick) {
        this.main = main;
        this.dropInventoryClick = dropInventoryClick;
    }

    public DropClickHandler[] getDropClickHandlers() {
        return dropInventoryClick;
    }

    public CacheableInventory getMain() {
        return main;
    }

    public static InventoryStorage getStorage() {
        return storage;
    }

    public static void setStorage(InventoryStorage inventoryStorage) {
        storage = inventoryStorage;
    }
}
