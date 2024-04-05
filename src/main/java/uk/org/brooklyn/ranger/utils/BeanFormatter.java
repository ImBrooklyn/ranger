package uk.org.brooklyn.ranger.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
public final class BeanFormatter {

    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final String SEPARATOR = SPACE.repeat(2);
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));

    @SneakyThrows
    public static String format(Printable bean) {
        if (bean == null) {
            return NEWLINE;
        }

        Class<? extends Printable> clazz = bean.getClass();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        final int maxLen = fields.stream().map(Field::getName)
                .map(String::length)
                .max(Comparator.comparingInt(Integer::valueOf))
                .orElse(0);

        if (maxLen <= 0) {
            return NEWLINE;
        }

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            String name = field.getName();
            field.setAccessible(true);
            Object val = field.get(bean);
            String value = switch (val) {
                case null -> EMPTY;
                case Date d -> DATE_FORMAT.get().format(d);
                default -> String.valueOf(val);
            };

            buffer.append(name)
                    .append(SPACE.repeat(maxLen - name.length()))
                    .append(SEPARATOR)
                    .append(value);
            if (i != (fields.size() - 1)) {
                buffer.append(NEWLINE);
            }
        }

        return buffer.toString();
    }
}
