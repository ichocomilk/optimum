package site.ichocomilk.optimum.spawners;

import java.util.Map;

import site.ichocomilk.optimum.spawners.data.Spawner;

public final class SpawnerStorage {

    private static SpawnerStorage storage;

    private final Map<String, Spawner> byName;
    private final Spawner[] spawners;

    SpawnerStorage(Spawner[] spawners, Map<String, Spawner> byName) {
        this.spawners = spawners;
        this.byName = byName;
    }

    public Spawner[] getSpawners() {
        return spawners;
    }

    public Spawner getByName(final String name) {
        return byName.get(name);
    }

    public static SpawnerStorage getStorage() {
        return storage;
    }

    public static void setStorage(SpawnerStorage newStorage) {
        storage = newStorage;
    }
}
