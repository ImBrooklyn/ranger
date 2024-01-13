package uk.org.brooklyn.ranger.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;
import uk.org.brooklyn.ranger.client.ZookeeperClient;

/**
 * @author ImBrooklyn
 * @since 13/01/2024
 */
@ShellComponent
public class ConnectivityCommand {

    private final ZookeeperClient zkClient;

    private static final String CMD_GROUP = "Connectivity commands";

    @Autowired
    public ConnectivityCommand(ZookeeperClient zkClient) {
        this.zkClient = zkClient;
    }

    @Bean
    public CommandRegistration connect() {
        return CommandRegistration.builder()
                .command("connect")
                .description("Connect to ZooKeeper.")
                .group(CMD_GROUP)
                .withTarget()
                .function(ctx -> zkClient.connect() ? "Connected..." : "Connecting failed...")
                .and()
                .build();
    }
}
