package site.ichocomilk.optimum.config.langs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import site.ichocomilk.optimum.config.ConfigManager;

public final class StartLangs {

    public void start(final ConfigManager manager) {
        final FileConfiguration config = manager.getPlugin().getConfig();
        final File langsFolder = getLangsFolder(manager);

        File[] langs = langsFolder.listFiles();
        int amountLangs = 0;
        String defaultLang = config.getString("lang.default-lang");
        boolean existDefaultLang = false;

        for (final File lang : langs) {
            final String name = lang.getName();
            if (!name.endsWith(".yml")) {
                continue;
            }
            amountLangs++;
            if (existDefaultLang || defaultLang == null) {
                continue;
            }
            if (name.contains(defaultLang)) {
                existDefaultLang = true;
            }
        }

        if (amountLangs == 0) {
            manager.create("langs", "en_US", "es_ES");
            Messages.setInstance(new Messages("en_US",loadLang(manager.load("langs/en_US.yml")), null));
            return;
        }

        if (!existDefaultLang) {
            manager.getPlugin().getLogger().warning("Default lang in config.yml doesn't exist in langs folder. Using en_US...");
            defaultLang = "en_US";
            manager.createIfAbsent("langs", defaultLang);
        }

        if (!config.getBoolean("lang.per-player")) {
            Messages.setInstance(new Messages(defaultLang, loadLang(manager.load("langs/"+defaultLang+".yml")), null));
            return;
        }

        final Map<String, Map<String, Object>> data = loadLangs(langs, amountLangs, manager);
        Map<String, Object> defaultLanguage = data.get(defaultLang);
        if (defaultLanguage == null) {
            defaultLanguage = data.values().iterator().next();
            defaultLang = data.keySet().iterator().next();
        }
        Messages.setInstance(new Messages(defaultLang, defaultLanguage, data));
    }

    private File getLangsFolder(final ConfigManager manager) {
        final File langsFolder = new File(manager.getPlugin().getDataFolder(), "langs");
        if (!langsFolder.exists()) {
            langsFolder.mkdir();
            manager.create("langs",
                "en_US", "es_ES"
            );
        }
        return langsFolder;
    }

    private Map<String, Map<String, Object>> loadLangs(
        final File[] langs,
        final int amountLangs,
        final ConfigManager manager
    ) {
        final Map<String, Map<String, Object>> data = new HashMap<>(amountLangs);

        for (final File langFile : langs) {
            String name = langFile.getName();
            if (!name.endsWith(".yml")) {
                continue;
            }
            final FileConfiguration config = manager.load(langFile);
            final Map<String, Object> messages = loadLang(config);
            if (messages.isEmpty()) {
                continue;
            }
            data.put(name.substring(0, name.length() - 4), messages);
            final List<String> sameLanguages = config.getStringList("same-languages");
            if (sameLanguages.isEmpty()) {
                continue;
            }
            for (final String lang : sameLanguages) {
                data.put(lang, messages);
            }
        }
        return data;
    }

    private Map<String, Object> loadLang(final FileConfiguration lang) {
        final Set<String> keys = lang.getKeys(false);
        final Map<String, Object> messages = new HashMap<>(keys.size());

        for (final String key : keys) {
            if (key.equals("same-languages")) {
                continue;
            }
            final Object value = lang.get(key);
            if (!(value instanceof MemorySection)) {
                messages.put(key, MessageColor.color(value));
                continue;
            }
            addMemorySection(
                key,
                ((MemorySection)value).getValues(false).entrySet(),
                messages,
                key.equals("inventory") || key.equals("spawner")
            );
        }
        return messages;
    }

    private void addMemorySection(
        String key,
        final Set<Entry<String,Object>> map,
        final Map<String, Object> output,
        final boolean inventorySection
    ) {
        key += '.';

        for (final Entry<String, Object> entry : map) {
            final Object value = entry.getValue();
            if (inventorySection) {
                if (value instanceof List) {
                    output.put(key + entry.getKey(), MessageColor.colorList(value));
                    continue;
                }
            }

            if (!(value instanceof MemorySection)) {
                output.put(key + entry.getKey(), MessageColor.color(value));
                continue;
            }
            addMemorySection(
                key + entry.getKey(),
                ((MemorySection)value).getValues(false).entrySet(),
                output,
                inventorySection
            );
        }
    }
}