package site.ichocomilk.optimum.listeners.types;

import java.util.concurrent.CompletableFuture;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;
import site.ichocomilk.optimum.spawners.updater.SpawnerUpdater;

public final class PlayerQuitListener implements EventListener {

    private final Database database;

    public PlayerQuitListener(Database database) {
        this.database = database;
    }

    @ListenerData(event = PlayerQuitEvent.class)
    public void handle(Event uncheckEvent) {
        final PlayerQuitEvent event = (PlayerQuitEvent)uncheckEvent;
        final PlayerData data = OptimumStorage.getStorage().getPlayers().remove(event.getPlayer().getUniqueId());
        if (data == null) {
            return;
        }
        SpawnerUpdater.saveDrops(data);
        if (database != null) {
            CompletableFuture.runAsync(() -> database.save(data, event.getPlayer().getName()));
        }
    }
}