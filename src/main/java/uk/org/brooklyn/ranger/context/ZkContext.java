package uk.org.brooklyn.ranger.context;

import org.springframework.stereotype.Component;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class ZkContext {
    private String cursor = "/";

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getCursor() {
        return this.cursor;
    }
}
