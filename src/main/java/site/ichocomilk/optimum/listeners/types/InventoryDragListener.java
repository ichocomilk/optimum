package site.ichocomilk.optimum.listeners.types;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;

import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;

public final class InventoryDragListener implements EventListener {

    @ListenerData(event = InventoryDragEvent.class)
    public void handle(Event uncheckEvent) {
        final InventoryDragEvent event = (InventoryDragEvent)uncheckEvent;
        if (event.getInventory().getHolder() instanceof OptimumInventoryHolder) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(event.getInventory().getType().getDefaultTitle());
        }
    }
}
