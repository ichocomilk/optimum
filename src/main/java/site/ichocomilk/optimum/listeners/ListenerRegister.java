package site.ichocomilk.optimum.listeners;

import java.lang.reflect.Method;

import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ListenerRegister {

    private static final EventExecutor EXECUTOR = (listener, event) -> ((EventListener)listener).handle(event);
    private final JavaPlugin plugin;

    public ListenerRegister(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final EventListener listener) {
        final ListenerData data = getData(listener);
        if (data == null) {
            plugin.getLogger().warning("Error on starting listener " + listener.getClass() + ". No method found with listener data annontation. Contact to the dev");
            return;
        }
        plugin.getServer().getPluginManager().registerEvent(data.event(), listener, data.priority(), EXECUTOR, plugin);
    }

    private ListenerData getData(final EventListener listener) {
        final Method[] methods = listener.getClass().getMethods();
        for (final Method method : methods) {
            final ListenerData data = method.getAnnotation(ListenerData.class);
            if (data != null) {
                return data;
            }
        }
        return null;
    }
}