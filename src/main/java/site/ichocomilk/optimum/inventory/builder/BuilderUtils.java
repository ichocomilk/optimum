package site.ichocomilk.optimum.inventory.builder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import site.ichocomilk.optimum.config.langs.Messages;
import site.ichocomilk.optimum.inventory.item.BuildeableItem;
import site.ichocomilk.optimum.inventory.item.ItemUtils;

final class BuilderUtils {
    
    public static void putItems(final String lang, final Inventory inventory, final BuildeableItem[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        for (final BuildeableItem necesary : items) {
            final ItemStack item = new ItemStack(necesary.material());
            ItemUtils.setMeta(
                item,
                Messages.getString(lang, necesary.langSection() + "name"),
                Messages.get(lang, necesary.langSection() + "lore")
            );
            inventory.setItem(necesary.slot(), item);
        }
    }
}
