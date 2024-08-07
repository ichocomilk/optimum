package site.ichocomilk.optimum.database.nitrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.FindOptions;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.UpdateOptions;
import org.dizitart.no2.filters.FluentFilter;
import org.dizitart.no2.transaction.Session;
import org.dizitart.no2.transaction.Transaction;

import site.ichocomilk.optimum.database.Database;
import site.ichocomilk.optimum.database.OptimumStorage;
import site.ichocomilk.optimum.database.PlayerData;
import site.ichocomilk.optimum.spawners.SpawnerStorage;
import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.Spawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawner;
import site.ichocomilk.optimum.spawners.data.player.PlayerSpawnerDrop;
import site.ichocomilk.optimum.upgrades.Upgrade;
import site.ichocomilk.optimum.upgrades.UpgradeStorage;
import site.ichocomilk.optimum.upgrades.modifiers.AmountModifier;
import site.ichocomilk.optimum.upgrades.modifiers.TimeModifier;

public final class NitriteDatabase implements Database {

    private final Nitrite nitrite;
    private final NitriteCollection players;

    NitriteDatabase(Nitrite nitrite) {
        this.nitrite = nitrite;
        this.players = nitrite.getCollection("players");
    }

    @Override
    public PlayerData get(String playerName) {
        final Document info = players.find(
            FluentFilter.where("name").eq(playerName),
            FindOptions.limitBy(1)
        ).firstOrNull();

        if (info == null) {
            return null;
        }

        final Set<String> fields = info.getFields();
        final Object upgrades = info.get("upgrades");
        if (upgrades != null) {
            fields.remove("upgrades");
        }
    
        final List<PlayerSpawner> spawners = getSpawners(info, fields);
        final PlayerData data = new PlayerData(spawners);

        if (!(upgrades instanceof Document)) {
            return data;
        }

        final List<TimeModifier> timeModifiers = new ArrayList<>();
        final List<AmountModifier> amountModifier = new ArrayList<>();
        getUpgrades((Document)upgrades, timeModifiers, amountModifier);
        if (!timeModifiers.isEmpty()) {
            data.setTimeUpgrades(timeModifiers);
        }
        if (!amountModifier.isEmpty()) {
            data.setAmountModifiers(amountModifier);
        }
        return data;
    }

    @Override
    public void save(final PlayerData data, final String playerName) {
        final Document document = saveData(data, playerName);
        final UpdateOptions options = new UpdateOptions();
        options.setInsertIfAbsent(true);
        players.update(FluentFilter.where("name").eq(playerName), document, options);
    }

    @Override
    public void saveAll() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.isEmpty()) {
            return;
        }
        try (Session session = nitrite.createSession()) {
            try (Transaction transaction = session.beginTransaction()) {
                final NitriteCollection collection = transaction.getCollection("players");
                final UpdateOptions options = new UpdateOptions();
                options.setInsertIfAbsent(true);

                for (final Player player : players) {
                    final PlayerData data = OptimumStorage.getStorage().getPlayers().get(player.getUniqueId());
                    if (data != null) {
                        final Document document = saveData(data, player.getName());
                        collection.update(FluentFilter.where("name").eq(player.getName()), document, options);
                    }
                }
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document saveData(final PlayerData data, final String playerName) {
        final Document document = Document.createDocument("name", playerName);
        setDrops(data.getSpawners(), document);
        setUpgrades(data.getTimeUpgrades(), document);
        setUpgrades(data.getAmountModifiers(), document);
        return document;
    }

    private List<PlayerSpawner> getSpawners(final Document info, final Set<String> spawnersNames) {

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

    private void setDrops(final List<PlayerSpawner> spawners, final Document document) {
        if (spawners == null) {
            return;
        }
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

    private void setUpgrades(final List<?> modifiers, final Document main) {
        final Document upgradesOut = Document.createDocument();

        for (final Object modifier : modifiers) {
            if (modifier instanceof Upgrade upgrade) {
                upgradesOut.put(upgrade.toString(), upgrade.serialize());        
            }
        }
        main.put("upgrades", upgradesOut);
    }

    private void getUpgrades(final Document upgrades, final List<TimeModifier> timeModifiers, final List<AmountModifier> amountModifiers) {
        final Set<String> fields = upgrades.getFields();
        for (final String field : fields) {
            final Upgrade creator = UpgradeStorage.getStorage().getUpgrade(field);
            if (creator == null) {
                continue;
            }
            final Object value = upgrades.get(field);
            if (!(value instanceof Map)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            final Map<String, Object> keyValue = (Map<String, Object>)value;
            final Upgrade upgrade = creator.deserialize(keyValue);
            if (upgrade instanceof TimeModifier) {
                timeModifiers.add((TimeModifier)upgrade);
                continue;
            }
            amountModifiers.add((AmountModifier)upgrade);
        }
    }

    @Override
    public void close() {
        nitrite.close();
    }
}