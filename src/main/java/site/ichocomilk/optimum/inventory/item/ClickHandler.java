package site.ichocomilk.optimum.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickHandler {
    void handle(final InventoryClickEvent event);
}
