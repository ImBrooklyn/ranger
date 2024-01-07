package uk.org.brooklyn.zkshell.context;

import org.springframework.stereotype.Component;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class ZkContext {
    private String cursor = "/";

    public ZkContext setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public String getCursor() {
        return this.cursor;
    }
}
