package uk.org.brooklyn.ranger.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Component;
import uk.org.brooklyn.ranger.client.ZookeeperClient;

/**
 * @author ImBrooklyn
 * @since 13/01/2024
 */
@Component
public record CommandAvailability(ZookeeperClient zkClient) {

    @Autowired
    public CommandAvailability {
    }

    public Availability connected() {
        return zkClient.connected()
                ? Availability.available()
                : Availability.unavailable("Zookeeper is currently not connected, run command 'connect' to connect...");
    }
}
