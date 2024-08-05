package site.ichocomilk.optimum.spawners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import site.ichocomilk.optimum.spawners.data.Spawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;

public final class SpawnerStorage {

    private static SpawnerStorage storage;

    private final Map<String, Spawner> byName;
    private final Spawner[] spawners;
    private final Map<UUID, List<PlayerSpawner>> players = new HashMap<>();

    SpawnerStorage(Spawner[] spawners, Map<String, Spawner> byName) {
        this.spawners = spawners;
        this.byName = byName;
    }

    public Spawner[] getSpawners() {
        return spawners;
    }

    public List<PlayerSpawner> remove(final Player player) {
        return this.players.remove(player.getUniqueId());
    }

    public List<PlayerSpawner> get(final UUID uuid) {
        return players.get(uuid);
    }

    public Spawner getByName(final String name) {
        return byName.get(name);
    }

    public void add(final Player player, final List<PlayerSpawner> spawner) {
        players.put(player.getUniqueId(), spawner);
    }

    public static SpawnerStorage getStorage() {
        return storage;
    }

    public static void setInstance(SpawnerStorage newStorage) {
        storage = newStorage;
    }
}
