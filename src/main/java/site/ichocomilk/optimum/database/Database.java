package site.ichocomilk.optimum.database;

import java.util.List;

import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;

public interface Database {

    List<PlayerSpawner> get(final String playerName);
    void save(final List<PlayerSpawner> spawners, final String playerName);
    void saveAll();
    void close();
}