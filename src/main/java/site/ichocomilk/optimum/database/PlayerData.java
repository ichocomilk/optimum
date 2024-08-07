package site.ichocomilk.optimum.database;

import java.util.List;

import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.upgrades.modifiers.AmountModifier;
import site.ichocomilk.optimum.upgrades.modifiers.TimeModifier;

public final class PlayerData {

    private final List<PlayerSpawner> spawners;
    private List<TimeModifier> timeUpgrades;
    private List<AmountModifier> amountModifiers;

    public PlayerData(List<PlayerSpawner> spawners) {
        this.spawners = spawners;
    }

    public List<TimeModifier> getTimeUpgrades() {
        return timeUpgrades;
    }

    public List<AmountModifier> getAmountModifiers() {
        return amountModifiers;
    }

    public void setAmountModifiers(List<AmountModifier> amountModifiers) {
        this.amountModifiers = amountModifiers;
    }

    public void setTimeUpgrades(List<TimeModifier> timeUpgrades) {
        this.timeUpgrades = timeUpgrades;
    }

    public List<PlayerSpawner> getSpawners() {
        return spawners;
    }
}
