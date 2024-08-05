package site.ichocomilk.optimum.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import site.ichocomilk.optimum.OptimumPlugin;

public final class ConfigManager {
  
    private final OptimumPlugin plugin;

    public ConfigManager(OptimumPlugin plugin) {
        this.plugin = plugin;
    }

    public OptimumPlugin getPlugin() {
        return plugin;
    }

    public YamlConfiguration load(final String file) {
        return load(new File(plugin.getDataFolder(), file));
    }
    public YamlConfiguration load(final File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
    public void create(String directory, final String... files) {
        if (directory != null && !directory.isEmpty()) {
            directory += '/';
        }
        for (final String f : files) {
            final String filePath = directory + f + ".yml";

            plugin.saveResource(filePath, false);
        }
    }
    public void createIfAbsent(String directory, final String... files) {
        if (directory != null && !directory.isEmpty()) {
            directory += '/';
        }
        for (final String f : files) {
            final String filePath = directory + f + ".yml";
            if (!new File(plugin.getDataFolder(), filePath).exists()) {
                plugin.saveResource(filePath, false);
            }
        }
    }
}
