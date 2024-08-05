package site.ichocomilk.optimum.listeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListenerData {

    EventPriority priority() default EventPriority.NORMAL;
    Class<? extends Event> event();
}
