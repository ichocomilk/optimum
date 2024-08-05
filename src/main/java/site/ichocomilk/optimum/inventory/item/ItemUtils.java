package site.ichocomilk.optimum.inventory.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils {

    @SuppressWarnings("unchecked")
    public static void setMeta(final ItemStack item, final String name, final Object lore) {
        if (name == "" && lore == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (name != "") {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            final List<String> parsedLore = (lore instanceof List<?>) ? (List<String>)lore : List.of(lore.toString());
            meta.setLore(parsedLore);
        }
        item.setItemMeta(meta);
    }
}