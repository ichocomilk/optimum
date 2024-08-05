package site.ichocomilk.optimum.inventory;

import java.util.Map;

import org.bukkit.entity.Player;

import site.ichocomilk.optimum.inventory.builder.SpawnerInventoryBuilder;
import site.ichocomilk.optimum.inventory.data.BuildeableInventory;
import site.ichocomilk.optimum.inventory.data.CacheableInventory;
import site.ichocomilk.optimum.inventory.item.ClickableItem;
import site.ichocomilk.optimum.inventory.item.DropClickHandler;

final class InventorySetup {

    private static final SpawnerInventoryBuilder SPAWNER_BUILDER = new SpawnerInventoryBuilder();

    public void setup(
        final DropClickHandler[] dropInventoryClick,
        final CacheableInventory main, final Map<String, Integer> mainNecesary,
        final BuildeableInventory spawner, final Map<String, Integer> spawnerNecesary,
        final BuildeableInventory drops, final Map<String, Integer> dropsNecesary
    ) {
        final ClickableItem spawnerItem = new ClickableItem(
            mainNecesary.get("spawner"),
            (event) -> SPAWNER_BUILDER.build((Player)event.getWhoClicked(), spawner, drops));
        main.holder().setClickableItems(spawnerItem);

        final ClickableItem backToMain = new ClickableItem(
            spawnerNecesary.get("back"),
            (event) -> main.open((Player)event.getWhoClicked()));
        spawner.holder().setClickableItems(backToMain);

        final ClickableItem backToSpawners = new ClickableItem(
            spawnerNecesary.get("back"),
            spawnerItem.getHandler());
        drops.holder().setClickableItems(backToSpawners);

        InventoryStorage.setStorage(new InventoryStorage(main, dropInventoryClick));
    }
}