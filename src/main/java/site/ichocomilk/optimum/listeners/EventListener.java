package site.ichocomilk.optimum.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface EventListener extends Listener {
    void handle(final Event uncheckEvent);
}
