package site.ichocomilk.optimum.upgrades;

import java.util.Map;

public final class UpgradeStorage {

    private static UpgradeStorage storage;

    private final Map<String, Upgrade> upgrades;

    UpgradeStorage(Map<String, Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public Upgrade getUpgrade(String name) {
        return upgrades.get(name);
    }

    public static UpgradeStorage getStorage() {
        return storage;
    }

    public static void setStorage(UpgradeStorage newstorage) {
        storage = newstorage;
    }
}
