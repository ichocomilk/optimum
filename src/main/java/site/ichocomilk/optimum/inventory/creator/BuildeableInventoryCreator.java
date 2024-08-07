package site.ichocomilk.optimum.inventory.creator;

import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.config.langs.MessageColor;
import site.ichocomilk.optimum.inventory.data.BuildeableInventory;
import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.inventory.item.BuildeableItem;

public final class BuildeableInventoryCreator {
    private final ConfigManager manager;

    public BuildeableInventoryCreator(ConfigManager manager) {
        this.manager = manager;
    }

    public BuildeableInventory create(
        final Map<String, Integer> necesaryItemsStorage,
        final String inventoryName,
        final String... necesary
    ) {
        manager.createIfAbsent("inventory", inventoryName);

        final FileConfiguration config = manager.load("inventory/"+inventoryName+".yml");
        int rows = config.getInt("rows");
        if (rows == 0) {
            manager.getPlugin().getLogger().warning("Rows in " + inventoryName + " inventory are 0. Setting in 6");
            rows = 6;
        }
        String title = MessageColor.color(config.get("title"));
        if (title == null) {
            manager.getPlugin().getLogger().warning("The inventory " + inventoryName + " doesn't have a title. Change in the section \"title\"");
            title = inventoryName;
        }
        return new BuildeableInventory(
            title,
            rows,
            getNecesary(necesaryItemsStorage, inventoryName, config, necesary),
            getExtra(inventoryName, config),
            new OptimumInventoryHolder()
        );
    }

    private BuildeableItem[] getNecesary(final Map<String, Integer> necesaryItems, final String inventoryName, final FileConfiguration config, final String... necesary) {
        final BuildeableItem[] items = new BuildeableItem[necesary.length];
        int i = 0;
        for (final String item : necesary) {
            final BuildeableItem buildeableItem = getItem(inventoryName, config, item);
            necesaryItems.put(item, buildeableItem.slot());
            items[i++] = buildeableItem;
        }
        return items;
    }

    private BuildeableItem[] getExtra(final String inventoryName, final FileConfiguration config) {
        final Object object = config.get("extra-items");
        if (object == null || !(object instanceof MemorySection)) {
            return null;
        }
        final MemorySection section = (MemorySection)object;
        final Set<String> keys = section.getKeys(false);
        final BuildeableItem[] items = new BuildeableItem[keys.size()];
        int i = 0;
        for (final String key : keys) {
            items[i] = getItem(inventoryName, config, "extra-items."+key);
        }
        return items;
    }    

    private BuildeableItem getItem(final String inventoryName, final FileConfiguration config, String section) {
        section += '.';
        String type = config.getString(section + "type");
        Material material;

        if (type == null || type.isEmpty() || (material = Material.getMaterial(type)) == null) {
            manager.getPlugin().getLogger().warning("The type of the item " + type + " doesn't exist. Check the inventory file " + inventoryName + ".yml" + ". Section: \"" + section + ".type\"");
            material = Material.STONE;
        }

        final String langSection = "inventory." + inventoryName + '.' + section;
        return new BuildeableItem(config.getInt(section + "slot"), material, langSection);
    }
}