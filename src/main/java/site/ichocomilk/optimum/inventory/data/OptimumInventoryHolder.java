package site.ichocomilk.optimum.inventory.data;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import site.ichocomilk.optimum.inventory.item.ClickableItem;

public class OptimumInventoryHolder implements InventoryHolder {

    private ClickableItem[] clickableItems;

    public final void clickSlot(final InventoryClickEvent event, final int slot) {
        if (clickableItems == null) {
            return;
        }
        for (final ClickableItem item : clickableItems) {
            if (item.hashCode() == slot) {
                item.getHandler().handle(event);
                break;
            }
        }
    }

    public final void setClickableItems(final ClickableItem... items) {
        this.clickableItems = items;
    }

    public final ClickableItem[] getClickableItems() {
        return clickableItems;
    }

    @Override
    public final Inventory getInventory() {
        return null;
    }
}