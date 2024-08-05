package site.ichocomilk.optimum.inventory.item;

import org.bukkit.Material;

public record BuildeableItem(
    int slot,
    Material material,
    String langSection
) {
}
