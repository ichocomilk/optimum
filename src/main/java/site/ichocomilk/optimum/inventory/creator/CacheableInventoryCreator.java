package site.ichocomilk.optimum.inventory.creator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.config.langs.MessageColor;
import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.inventory.data.CacheableInventory;
import site.ichocomilk.optimum.inventory.data.OptimumInventoryHolder;
import site.ichocomilk.optimum.inventory.item.ItemUtils;

public final class CacheableInventoryCreator {
    
    private final ConfigManager manager;

    public CacheableInventoryCreator(ConfigManager manager) {
        this.manager = manager;
    }

    public CacheableInventory create(
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
        final OptimumInventoryHolder holder = new OptimumInventoryHolder();

        final Collection<String> langs = Messages.getLangs();
        final String defaultLang = Messages.getDefaultLang();
        if (langs == null) {
            final Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
            setItems(necesaryItemsStorage, inventoryName, defaultLang, config, inventory, necesary);
            return new CacheableInventory(null, inventory, holder);
        }

        final Map<String, Inventory> inventories = new HashMap<>(langs.size());
        for (final String lang : langs) {
            final Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
            setItems(necesaryItemsStorage, inventoryName, lang, config, inventory, necesary);
            inventories.put(lang, inventory);
        }

        return new CacheableInventory(inventories, inventories.get(defaultLang), holder);
    }

    private void setItems(
        final Map<String, Integer> necesaryItemsStorage,
        final String inventoryName,
        final String lang,
        final FileConfiguration config,
        final Inventory inventory,
        final String... necesary
    ) {
        for (final String item : necesary) {
            necesaryItemsStorage.put(item, setItem(config, lang, inventoryName, inventory, item));    
        }

        final Object object = config.get("extra-items");
        if (object == null || !(object instanceof MemorySection)) {
            return;
        }
        final MemorySection section = (MemorySection)object;
        final Set<String> keys = section.getKeys(false);
        for (final String item : keys) {
            setItem(config, lang, inventoryName, inventory, "extra-items."+item);
        }
    }

    private int setItem(
        final FileConfiguration config,
        final String lang,
        final String inventoryName,
        final Inventory inventory,
        String section
    ) {
        final String langSection = "inventory." + inventoryName + '.' + section;

        section += '.';
        String type = config.getString(section + "type");
        Material material;

        if (type == null || type.isEmpty() || (material = Material.getMaterial(type)) == null) {
            manager.getPlugin().getLogger().warning("The type of the item " + type + " doesn't exist. Check the inventory file " + inventoryName + ".yml" + ". Section: \"" + section + ".type\"");
            material = Material.STONE;
        }
        final ItemStack item = new ItemStack(material);

        ItemUtils.setMeta(
            item,
            Messages.getString(lang, langSection + ".name"),
            Messages.get(lang, langSection + ".lore")
        );

        final int slot = config.getInt(section + "slot");
        inventory.setItem(slot, item);
        return slot;
    }
}
