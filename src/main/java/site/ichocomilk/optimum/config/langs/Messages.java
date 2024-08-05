package site.ichocomilk.optimum.config.langs;

import java.util.Collection;
import java.util.Map;

import org.bukkit.entity.Player;

public final class Messages {

    private static Messages instance;

    private String defaultLanguage;
    private final Map<String, Object> defaultLang;
    private final Map<String, Map<String, Object>> langs;

    Messages(String defaultLanguage, Map<String, Object> defaultLang, Map<String, Map<String, Object>> langs) {
        this.defaultLanguage = defaultLanguage;
        this.defaultLang = defaultLang;
        this.langs = langs;
    }

    public static Object get(final String lang, final String key) {
        if (lang == null || instance.langs == null) {
            return instance.defaultLang.get(key);
        }

        final Map<String, Object> language = instance.langs.get(lang);
        final Object message = (language == null)
            ? instance.defaultLang.get(key)
            : language.get(key);

        return message;
    }

    public static String getString(final String lang, final String key) {
        final Object value = get(lang, key);
        return (value instanceof String) ? value.toString() : "";
    }

    public static Collection<String> getLangs() {
        return instance.langs == null ? null : instance.langs.keySet();
    }

    public static String getDefaultLang() {
        return instance.defaultLanguage;
    }

    public static void send(final Player player, final String key) {
        final Object message = get(player.spigot().getLocale(), key);
        if (message != null && message instanceof String) {
            player.sendMessage(message.toString());
        }
    }

    public static void setInstance(Messages newInstance) {
        instance = newInstance;
    }
}