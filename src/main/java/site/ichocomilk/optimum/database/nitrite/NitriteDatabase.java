package site.ichocomilk.optimum.database.nitrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.FindOptions;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.UpdateOptions;
import org.dizitart.no2.filters.FluentFilter;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.Spawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;

public final class NitriteDatabase implements Database {

    private final Nitrite nitrite;
    private final NitriteCollection players;

    NitriteDatabase(Nitrite nitrite) {
        this.nitrite = nitrite;
        this.players = nitrite.getCollection("players");
    }

    @Override
    public List<PlayerSpawner> get(String playerName) {
        final Document info = players.find(
            FluentFilter.where("name").eq(playerName),
            FindOptions.limitBy(1)
        ).firstOrNull();

        if (info == null) {
            return null;
        }

        final Set<String> spawnersNames = info.getFields();
        final List<PlayerSpawner> spawners = new ArrayList<>(spawnersNames.size());

        for (final String spawnerName : spawnersNames) {
            final Object unchecked = info.get(spawnerName);
            if (!(unchecked instanceof Document[])) {
                continue;
            }
            final Spawner spawner = SpawnerStorage.getStorage().getByName(spawnerName);
            if (spawner == null) {
                continue;
            }
    
            final PlayerSpawner playerSpawner = new PlayerSpawner(spawner);
            final Document[] drops = (Document[])unchecked;
            playerSpawner.addSpawners(((Number)drops[0].get("amount")).intValue());
 
            int i = 1;
            for (; i < drops.length; i++) {
                final Document drop = drops[i];
                final Number amount = (Number)drop.get("amount");
                final String type = (String)drop.get("type");
                
                int dropIndex = 0;
                PlayerSpawnerDrop playerSpawnerDrop = null;
                for (final Drop spawnerDrop : spawner.getDrops()) {
                    if (spawnerDrop.toString().equals(type)) {
                        playerSpawnerDrop = playerSpawner.getDrops()[dropIndex];
                        break;
                    }
                    dropIndex++;
                }
                if (playerSpawnerDrop == null) {
                    continue;
                }
                playerSpawnerDrop.amount = amount.intValue();
            }

            spawners.add(playerSpawner);
        }
        return spawners;
    }

    @Override
    public void save(final List<PlayerSpawner> spawners, final String playerName) {
        final Document document = Document.createDocument("name", playerName);
        setDrops(spawners, document);

        final UpdateOptions options = new UpdateOptions();
        options.setInsertIfAbsent(true);
    
        players.update(FluentFilter.where("name").eq(playerName), document, options);
    }

    private void setDrops(final List<PlayerSpawner> spawners, final Document document) {
        for (final PlayerSpawner spawner : spawners) {
            final Drop[] drops = spawner.getSpawner().getDrops();
            final Document[] dropsDocuments = new Document[drops.length + 1];
            dropsDocuments[0] = Document.createDocument().put("amount", spawner.getSpawnersAmount());

            int i = 1;
            for (final Drop drop : drops) {
                dropsDocuments[i] = Document.createDocument()
                    .put("type", drop.toString())
                    .put("amount", spawner.getDrops()[i-1].amount);
                i++;
            }
            document.put(spawner.getSpawner().toString(), dropsDocuments);
        }
    }

    @Override
    public void saveAll() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (final Player player : players) {
            final List<PlayerSpawner> spawners = SpawnerStorage.getStorage().get(player.getUniqueId());
            if (spawners != null) {
                save(spawners, player.getName());
            }
        }
    }

    @Override
    public void close() {
        nitrite.close();
    }
}