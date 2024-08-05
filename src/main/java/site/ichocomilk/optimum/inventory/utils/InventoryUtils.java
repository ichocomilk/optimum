package site.ichocomilk.optimum.inventory.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class InventoryUtils {

    public static int addItem(final PlayerInventory inventory, final Material material, int amount) {
        if (amount == 0) {
            return 0;
        }
        final int size = inventory.getSize();
        final ItemStack addItem = new ItemStack(material);

        for (int i = 0; i < size; i++) {
            final ItemStack item = inventory.getItem(i);
            if (item == null) {
                if (amount <= 64) {
                    addItem.setAmount(amount);
                    inventory.setItem(i, addItem);
                    return 0;
                }
                addItem.setAmount(64);
                inventory.setItem(i, addItem);
                if ((amount-=64) == 0) {
                    return 0;
                }
                continue;
            }
            if (item.getAmount() == 64 || item.getType() != material || item.hasItemMeta()) {
                continue;
            }
            final int itemsToAdd = amount + item.getAmount();
            if (itemsToAdd > 64) {
                amount -= (64 - item.getAmount());

                item.setAmount(64);
                inventory.setItem(i, item);
                if (amount == 0) {
                    return 0;
                }
                continue;
            }
            item.setAmount(itemsToAdd);
            inventory.setItem(i, item);
            return 0;
        }
        return amount;
    }

}
