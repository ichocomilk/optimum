package site.ichocomilk.optimum.config.langs;

import java.util.ArrayList;
import java.util.List;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

public final class MessageColor {

    public static String color(final Object object) {
        if (object == null) {
            return null;
        }
        String message = object.toString();

        if (object instanceof List<?>) {
            final List<?> list = (List<?>)object;
            final StringBuilder builder = new StringBuilder();
            int size = list.size();
            int i = 0;
            for (final Object object2 : list) {
                builder.append(object2);
                if (++i == size) {
                    continue;
                }
                builder.append('\n');
            }
            message = builder.toString();
        }

        if (message.isEmpty() || message.isBlank()) {
            return null;
        }
        return IridiumColorAPI.process(message);
    }

    public static List<String> colorList(final Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof List<?>)) {
            return List.of(color(object.toString()));
        }
        final List<?> listObjects = (List<?>)object;
        final List<String> lines = new ArrayList<>(listObjects.size());
        for (final Object unknown : listObjects) {
            lines.add(color(unknown));
        }
        return lines;
    }
}
