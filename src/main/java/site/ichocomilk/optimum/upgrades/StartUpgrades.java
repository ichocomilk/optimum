package site.ichocomilk.optimum.upgrades;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.upgrades.types.BoosterUpgrade;
import site.ichocomilk.optimum.upgrades.types.TimeMachineUpgrade;

public final class StartUpgrades {

    public void start(final ConfigManager manager) {
        manager.createIfAbsent(null, "upgrades");

        final FileConfiguration upgrades = manager.load("upgrades.yml");
        final Map<String, Upgrade> mapUpgrades = new HashMap<>(2);

        if (upgrades.getBoolean("booster.enable")) {
            mapUpgrades.put("booster", new BoosterUpgrade(new BoosterUpgrade.BoosterData(
                upgrades.getInt("booster.max-level"),
                upgrades.getInt("booster.percentage-cost-increase-per-level"),
                upgrades.getInt("booster.upgrade-per-level"),
                upgrades.getInt("boosters.cost"),
                upgrades.getString("booster.permission")
            )));
        }

        if (upgrades.getBoolean("time-machine.enable")) {
            mapUpgrades.put("time-machine", new TimeMachineUpgrade(new TimeMachineUpgrade.TimeData(
                upgrades.getInt("time-machine.max-level"),
                upgrades.getInt("time-machine.percentage-cost-increase-per-level"),
                upgrades.getInt("time-machine.seconds-to-skip-per-level"),
                upgrades.getInt("time-machine.cost"),
                upgrades.getString("time-machine.permission")
            )));
        }
        UpgradeStorage.setStorage(new UpgradeStorage(mapUpgrades));
    }
}