package site.ichocomilk.optimum.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class OptimumStorage {
    private static final OptimumStorage STORAGE = new OptimumStorage();

    private final Map<UUID, PlayerData> players = new HashMap<>();

    public Map<UUID, PlayerData> getPlayers() {
        return players;
    }

    public void clear() {
        players.clear();
    }

    public static OptimumStorage getStorage() {
        return STORAGE;
    }
}