package site.ichocomilk.optimum.inventory.data;

import site.ichocomilk.optimum.inventory.item.BuildeableItem;

public final record BuildeableInventory(
    String title,
    int rows,
    BuildeableItem[] necesaryItem,
    BuildeableItem[] extraItems,
    OptimumInventoryHolder holder
) {
}
