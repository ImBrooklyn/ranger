package uk.org.brooklyn.ranger.utils;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
public interface Printable extends Serializable {
    default String print() {
        return BeanFormatter.format(this);
    }
}
