package site.ichocomilk.optimum.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;

import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;

public interface DropClickHandler {
    boolean handle(final PlayerSpawnerDrop playerSpawnerDrop, final Drop drop, final InventoryClickEvent event);
}
