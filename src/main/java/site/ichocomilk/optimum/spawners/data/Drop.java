package site.ichocomilk.optimum.spawners.data;

import org.bukkit.Material;

public final class Drop {

    private final Material material;
    private final int amount;
    private final double secondsToGenerate;
    private final int max;
    private final double sellPrice;

    private final int id;
    private final String name;

    public Drop(
        int id,
        Material material,
        int amount,
        double secondsToGenerate,
        int max,
        double sellPrice,
        String name
    ) {
        this.id = id;
        this.material = material;
        this.amount = amount;
        this.secondsToGenerate = secondsToGenerate;
        this.max = max;
        this.sellPrice = sellPrice;
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMax() {
        return max;
    }

    public int getAmount() {
        return amount;
    }

    public double getSecondsToGenerate() {
        return secondsToGenerate;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    @Override
    public final String toString() {
        return name;
    }

    @Override
    public final int hashCode() {
        return id;
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        return (other instanceof Drop) ? ((Drop) other).id == this.id : false;
    }
}
