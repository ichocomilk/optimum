package site.ichocomilk.optimum.listeners.types;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.updater.SpawnerUpdater;

public final class PlayerQuitListener implements EventListener {

    private final Database database;

    public PlayerQuitListener(Database database) {
        this.database = database;
    }

    @ListenerData(event = PlayerQuitEvent.class)
    public void handle(Event uncheckEvent) {
        final PlayerQuitEvent event = (PlayerQuitEvent)uncheckEvent;
        final List<PlayerSpawner> spawners = SpawnerStorage.getStorage().remove(event.getPlayer());
        if (spawners == null) {
            return;
        }
        SpawnerUpdater.saveDrops(spawners);
        CompletableFuture.runAsync(() -> database.save(spawners, event.getPlayer().getName()));
    }
}
