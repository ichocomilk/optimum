package site.ichocomilk.optimum.listeners.types;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;

public final class PlayerJoinListener implements EventListener {

    private final Database database;

    public PlayerJoinListener(Database database) {
        this.database = database;
    }

    @ListenerData(event = PlayerJoinEvent.class)
    public void handle(Event uncheckEvent) {
        final PlayerJoinEvent event = (PlayerJoinEvent)uncheckEvent;
            final List<PlayerSpawner> spawners = database.get(event.getPlayer().getName());
            if (spawners == null) {
                return;
            }
            SpawnerStorage.getStorage().add(event.getPlayer(), spawners);
    }
}