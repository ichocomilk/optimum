package site.ichocomilk.optimum.upgrades.types;

import java.util.Map;

import site.ichocomilk.optimum.upgrades.Upgrade;
import site.ichocomilk.optimum.upgrades.modifiers.AmountModifier;

public final class BoosterUpgrade implements AmountModifier, Upgrade {

    public static record BoosterData(
        int maxLevel,
        int percentageCostIncrease,
        int upgradePerLevel,
        int initialCost,
        String permission
    ) {}

    private final BoosterData data;
    private int level;

    public BoosterUpgrade(BoosterData data) {
        this.data = data;
    }

    @Override
    public int handle(final int amountToGenerate) {
        return amountToGenerate + ((data.upgradePerLevel * level) * amountToGenerate) / 100;
    }

    @Override
    public boolean levelUp(final int money) {
        if (level >= data.maxLevel) {
            return false;
        }
        final int cost = data.initialCost + ((data.percentageCostIncrease * level) * data.initialCost) / 100;
        if (money >= cost) {
            level++;
            return true;
        }
        return false;
    }

    @Override
    public Upgrade deserialize(Map<String, Object> in) {
        Object level = in.get("level");
        if (level == null) {
            return null;
        }
        final BoosterUpgrade upgrade = new BoosterUpgrade(data);
        upgrade.level = ((Number)level).intValue();
        return upgrade;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("level", level);
    }

    @Override
    public String toString() {
        return "booster";
    }
}