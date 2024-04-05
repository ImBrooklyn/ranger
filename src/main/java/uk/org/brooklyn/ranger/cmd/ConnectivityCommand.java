package uk.org.brooklyn.ranger.cmd;

import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;

/**
 * @author ImBrooklyn
 * @since 13/01/2024
 */
@ShellComponent
public class ConnectivityCommand extends CommandBase {

    private static final String CMD_GROUP = "Connectivity commands";

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
