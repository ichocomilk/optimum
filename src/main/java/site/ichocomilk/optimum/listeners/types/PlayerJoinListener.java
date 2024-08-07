package site.ichocomilk.optimum.listeners.types;

import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.listeners.EventListener;
import site.ichocomilk.optimum.listeners.ListenerData;

public final class PlayerJoinListener implements EventListener {

    private final Database database;

    public PlayerJoinListener(Database database) {
        this.database = database;
    }

    @ListenerData(event = PlayerJoinEvent.class)
    public void handle(Event uncheckEvent) {
        if (database == null) {
            return;
        }

        final Player player = ((PlayerJoinEvent)uncheckEvent).getPlayer();
        CompletableFuture.runAsync(() -> {
            final PlayerData data = database.get(player.getName());
            if (data == null) {
                return;
            }
            OptimumStorage.getStorage().getPlayers().put(player.getUniqueId(), data);
        });
    }
}