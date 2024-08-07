package site.ichocomilk.optimum.upgrades.types;

import java.util.Map;

import site.ichocomilk.optimum.upgrades.Upgrade;
import site.ichocomilk.optimum.upgrades.modifiers.TimeModifier;

public final class TimeMachineUpgrade implements TimeModifier, Upgrade {

    public static record TimeData(
        int maxLevel,
        int percentageCostIncrease,
        int secondsToSkip,
        int initialCost,
        String permission
    ) {}

    private final TimeData data;
    private int level;

    public TimeMachineUpgrade(TimeData data) {
        this.data = data;
    }

    @Override
    public long handle(final long diferenceTime) {
        return diferenceTime + level * data.secondsToSkip;
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
        final TimeMachineUpgrade upgrade = new TimeMachineUpgrade(data);
        upgrade.level = ((Number)level).intValue();
        return upgrade;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("level", level);
    }

    @Override
    public String toString() {
        return "time-machine";
    }
}