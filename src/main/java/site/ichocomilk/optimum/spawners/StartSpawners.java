package site.ichocomilk.optimum.spawners;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import site.ichocomilk.optimum.config.ConfigManager;
import site.ichocomilk.optimum.config.langs.MessageColor;
import site.ichocomilk.optimum.spawners.data.Drop;
import site.ichocomilk.optimum.spawners.data.Spawner;

public final class StartSpawners {

    private final ConfigManager manager;

    public StartSpawners(ConfigManager manager) {
        this.manager = manager;
    }

    public void start() {
        final File spawnersFolder = new File(manager.getPlugin().getDataFolder(), "spawners");
        if (!spawnersFolder.exists()) {
            manager.create("spawners",
            "zombie"
            );
        }
        String defaultTitle = null;
        if (!manager.getPlugin().getConfig().getBoolean("spawners.invididual-inventory-name")) {
            defaultTitle = MessageColor.color(manager.getPlugin().getConfig().getString("spawners.inventory-name"));
        }

        File[] files = spawnersFolder.listFiles();
        if (files.length == 0) {
            return;
        }
        final Spawner[] spawners = new Spawner[files.length];
        int i = 0;
        for (final File file : files) {
            spawners[i++] = createSpawner(defaultTitle, manager.load(file), file.getName());
        }
        final Map<String, Spawner> byName = new HashMap<>(spawners.length);
        for (final Spawner spawner : spawners) {
            byName.put(spawner.toString(), spawner);
        }

        if (spawners.length <= 40) {
            SpawnerStorage.setStorage(new SpawnerStorage(spawners, byName));
            return;        
        }
        // Sort for binary search
        Arrays.sort(spawners, (first, second) -> {
            if (first.hashCode() == second.hashCode()) {
                return 0;
            }
            if (first.hashCode() < second.hashCode()) {
                return -1;
            }
            return -1;
        });
        SpawnerStorage.setStorage(new SpawnerStorage(spawners, byName));
    }

    private Spawner createSpawner(final String defaultTitle, final FileConfiguration spawner, final String fileName) {
        String type = spawner.getString("unlock-item");
        Material material;

        if (type == null || type.isEmpty() || (material = Material.getMaterial(type)) == null) {
            manager.getPlugin().getLogger().warning("The material " + type + " for the spawner inventory doesn't exist. Check in spawners folder the file " + fileName + " in the section \"spawner-type\"");
            material = Material.STONE;
        }
        String title = (defaultTitle == null) ? MessageColor.color(spawner.getString("inventory-title")) : defaultTitle;
        if (title == null) {
            manager.getPlugin().getLogger().warning("The inventory name of the spawner " + fileName + " doesn't exist. Using: " + fileName + ". Change in the section \"inventory-title\"");
            title = fileName;
        }
     
        final Drop[] drops = createDrops(spawner, fileName);
        if (drops == null) {
            manager.getPlugin().getLogger().warning("The spawner " + fileName + " doesn't have any drop. Is useless, try add some drops in the section \"drops\"");
        }
        return new Spawner(
            fileName.substring(0, fileName.length() - 4),
            title,
            material,
            drops
        );
    }

    private Drop[] createDrops(final FileConfiguration spawner, final String fileName) {
        final Object object = spawner.get("drops");
        if (!(object instanceof MemorySection)) {
            return null;
        }
        final MemorySection section = ((MemorySection)object);
        final Set<String> drops = section.getKeys(false);
        final Drop[] parsedDrops = new Drop[drops.size()];
        int i = 0;

        for (final String drop : drops) {
            String type = section.getString(drop + ".type");
            Material material;
    
            if (type == null || type.isEmpty() || (material = Material.getMaterial(type)) == null) {
                manager.getPlugin().getLogger().warning("The material " + type + " for the spawner drop doesn't exist. Check in spawners folder the file " + fileName + " in the section \"drops." + drop + ".type\"");
                material = Material.STONE;
            }
            int amount = section.getInt(drop + ".amount");
            if (amount == 0) {
                amount = 1;
            }
            double seconds = section.getDouble(drop + ".seconds");
            if (seconds == 0) {
                seconds = 1;
            }
            int max = section.getInt(drop + ".max");
            if (max < 1) {
                manager.getPlugin().getLogger().warning("The max amount of the drop " + drop + " Isn't in the range 1-2.147.483.647 | Spawner: " + fileName + " in the section \"drops." + drop + ".max\". Setting max in 10000...");
                max = 10000;
            }
            parsedDrops[i] = new Drop(
                i++,
                material,
                amount,
                seconds,
                max,
                section.getDouble(drop + ".sell-price"),
                drop
            );
        }
        return parsedDrops;
    }
}