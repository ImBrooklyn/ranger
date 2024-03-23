package uk.org.brooklyn.ranger.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;
import uk.org.brooklyn.ranger.client.ZookeeperClient;
import uk.org.brooklyn.ranger.context.CommandAvailability;
import uk.org.brooklyn.ranger.context.ZkContext;
import uk.org.brooklyn.ranger.model.NodeStat;
import uk.org.brooklyn.ranger.utils.BeanFormatter;

import java.util.stream.Collectors;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
@ShellComponent
public class ReadCommand {

    private final ZkContext zkContext;

    private final CommandAvailability cmdAvailability;

    private final ZookeeperClient zkClient;

    private static final String CMD_GROUP = "Basic read commands";

    @Autowired
    public ReadCommand(ZkContext zkContext, CommandAvailability cmdAvailability, ZookeeperClient zkClient) {
        this.zkContext = zkContext;
        this.cmdAvailability = cmdAvailability;
        this.zkClient = zkClient;
    }

    @Bean
    public CommandRegistration stat() {
        return CommandRegistration.builder()
                .command("stat", "info")
                .description("Stat node.")
                .availability(cmdAvailability::connected)
                .group(CMD_GROUP)
                .withOption()
                .position(0)
                .longNames("node")
                .type(String.class)
                .completion(ctx -> zkClient.children(zkContext.getCursor())
                        .stream().map(CompletionProposal::new)
                        .collect(Collectors.toList()))
                .and()
                .withTarget()
                .function(this::statNode)
                .and()
                .build();
    }

    private String statNode(CommandContext ctx) {
        String node = zkContext.getNode(ctx);
        if (node == null) {
            return null;
        }
        NodeStat nodeStat = zkClient.stat(node);
        return nodeStat.print();
    }
}
