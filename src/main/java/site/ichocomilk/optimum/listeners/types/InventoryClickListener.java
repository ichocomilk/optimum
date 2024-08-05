package site.ichocomilk.optimum.listeners.types;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;

public final class InventoryClickListener implements EventListener {

    @ListenerData(event = InventoryClickEvent.class)
    public void handle(Event uncheckEvent) {
        final InventoryClickEvent event = (InventoryClickEvent)uncheckEvent;

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getInventory().getHolder() instanceof OptimumInventoryHolder) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getClickedInventory().getHolder() instanceof OptimumInventoryHolder)) {
            return;
        }
        final OptimumInventoryHolder inventory = (OptimumInventoryHolder)event.getClickedInventory().getHolder();
        inventory.clickSlot(event, event.getSlot());
        event.setCancelled(true);
    }
}