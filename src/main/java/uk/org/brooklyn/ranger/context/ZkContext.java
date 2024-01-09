package uk.org.brooklyn.ranger.context;

import org.springframework.stereotype.Component;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class ZkContext {
    private String cursor = "/";
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getCursor() {
        return this.cursor;
    }
}
