package site.ichocomilk.optimum.inventory.builder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.inventory.InventoryStorage;
import site.ichocomilk.optimum.inventory.data.BuildeableInventory;
import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.inventory.item.ClickableItem;
import site.ichocomilk.optimum.inventory.item.DropClickHandler;
import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;
import site.ichocomilk.optimum.spawners.updater.SpawnerUpdater;

public final class DropInventoryBuilder {

    private static final void handle(final Player player, final Inventory inventory, final int slot ,final PlayerSpawnerDrop playerSpawnerDrop, final Drop drop, final InventoryClickEvent event) {
        final DropClickHandler handler = InventoryStorage.getStorage().getDropClickHandlers()[event.getClick().ordinal()];
        if (handler != null && handler.handle(playerSpawnerDrop, drop, event)) {
            final String amountMessage = Messages.getString(player.spigot().getLocale(), "spawner.amount");
            final ItemStack itemstack =  new ItemStack(drop.getMaterial());
            final ItemMeta meta = itemstack.getItemMeta();

            meta.setDisplayName(amountMessage.replace("%amount%", String.valueOf(playerSpawnerDrop.amount)));
            itemstack.setItemMeta(meta);
            inventory.setItem(slot, itemstack);
        }
    }

    public void build(final PlayerData data, final Player player, final PlayerSpawner spawner, final BuildeableInventory dropInventory) {
        final OptimumInventoryHolder holder = new OptimumInventoryHolder();
        final Inventory inventory = Bukkit.createInventory(
            holder,
            6*9,
            spawner.getSpawner().getInventoryTitle());

        final ClickableItem[] baseItems = dropInventory.holder().getClickableItems();
        final ClickableItem[] clickableItems = new ClickableItem[spawner.getSpawner().getDrops().length + baseItems.length];

        BuilderUtils.putItems(player.spigot().getLocale(), inventory, dropInventory.extraItems());
        BuilderUtils.putItems(player.spigot().getLocale(), inventory, dropInventory.necesaryItem());

        final long time = System.currentTimeMillis();
        final PlayerSpawnerDrop[] drops = spawner.getDrops();
        final String amountMessage = Messages.getString(player.spigot().getLocale(), "spawner.amount");
    
        int i = 0;
        for (final Drop drop : spawner.getSpawner().getDrops()) {
            SpawnerUpdater.calculateDrop(time, data, drop, drops[i], spawner.getSpawnersAmount());

            final ItemStack item =  new ItemStack(drop.getMaterial());
            final ItemMeta meta = item.getItemMeta();
            final PlayerSpawnerDrop playerSpawnerDrop = drops[i];

            meta.setDisplayName(amountMessage.replace("%amount%", String.valueOf(playerSpawnerDrop.amount)));
            item.setItemMeta(meta);
            final int slot = i;
            clickableItems[i] = new ClickableItem(i, (event) -> handle(player, inventory, slot, playerSpawnerDrop, drop, event));
            inventory.setItem(i++, item);
        }

        for (final ClickableItem base : baseItems) {
            clickableItems[i++] = base;
        }

        holder.setClickableItems(clickableItems);
        player.openInventory(inventory);
    }
}
