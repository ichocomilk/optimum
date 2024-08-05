package site.ichocomilk.optimum.spawners.data;

import org.bukkit.Material;

public final class Spawner {

    private final String name;
    private final String inventoryTitle;
    private final Material unlockItem;
    private final Drop[] drops;
    private final int hash;

    public Spawner(String name, String inventoryTitle, Material unlockItem, Drop[] drops) {
        this.name = name;
        this.inventoryTitle = inventoryTitle;
        this.unlockItem = unlockItem;
        this.hash = unlockItem.ordinal();
        this.drops = drops;
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }
    public Material getItem() {
        return unlockItem;
    }
    public Drop[] getDrops() {
        return drops;
    }

    @Override
    public final String toString() {
        return name;
    }
    @Override
    public final int hashCode() {
        return hash;
    }
    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof Spawner) ? ((Spawner)obj).hash == this.hash : false;
    }
}