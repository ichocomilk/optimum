package site.ichocomilk.optimum.inventory.data;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final record CacheableInventory(
    Map<String, Inventory> languagesInventory,
    Inventory defaultInventory,
    OptimumInventoryHolder holder
) {
    public void open(final Player player) {
        if (languagesInventory == null) {
            player.openInventory(defaultInventory);
            return;
        }
        final Inventory inventory = languagesInventory.get(player.spigot().getLocale());
        if (inventory == null) {
            player.openInventory(defaultInventory);
            return;
        }
        player.openInventory(inventory);
    }
}