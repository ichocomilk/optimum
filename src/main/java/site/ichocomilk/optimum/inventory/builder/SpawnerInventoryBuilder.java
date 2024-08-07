package site.ichocomilk.optimum.inventory.builder;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.inventory.data.BuildeableInventory;
import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.inventory.item.ClickableItem;
import site.ichocomilk.optimum.inventory.item.ItemUtils;
import site.ichocomilk.optimum.spawners.data.Spawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;

public final class SpawnerInventoryBuilder {

    private static final DropInventoryBuilder DROP_INVENTORY_BUILDER = new DropInventoryBuilder();

    public void build(final Player player, final BuildeableInventory buildeableInventory, final BuildeableInventory dropInventory) {
        final PlayerData data = OptimumStorage.getStorage().getPlayers().get(player.getUniqueId());
        if (data == null) {
            Messages.send(player, "need-add-spawners");
            return;
        }
        final List<PlayerSpawner> spawners = data.getSpawners();
        final OptimumInventoryHolder holder = new OptimumInventoryHolder();
        final Inventory inventory = Bukkit.createInventory(holder, buildeableInventory.rows() * 9, buildeableInventory.title());
        final String lang = player.spigot().getLocale();

        BuilderUtils.putItems(lang, inventory, buildeableInventory.necesaryItem());
        BuilderUtils.putItems(lang, inventory, buildeableInventory.extraItems());

        final ClickableItem[] baseItems = buildeableInventory.holder().getClickableItems();
        final ClickableItem[] clickableItems = new ClickableItem[spawners.size() + baseItems.length];
    
        int slot = 0;
        for (final PlayerSpawner spawner : spawners) {
            final Spawner base = spawner.getSpawner();
            final ItemStack item = new ItemStack(base.getItem());
            ItemUtils.setMeta(
                item,
                Messages.getString(lang, "spawner."+base.toString()+".name"),
                Messages.get(lang, "spawner."+base.toString()+".lore")
            );

            clickableItems[slot] = new ClickableItem(
                slot,
                (p) -> DROP_INVENTORY_BUILDER.build(data, player, spawner, dropInventory));
            inventory.setItem(slot++, item);
        }
        for (final ClickableItem base : baseItems) {
            clickableItems[slot++] = base;
        }

        holder.setClickableItems(clickableItems);
        player.openInventory(inventory);
    }
}