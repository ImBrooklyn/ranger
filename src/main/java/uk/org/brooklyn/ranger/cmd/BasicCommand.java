package uk.org.brooklyn.ranger.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import uk.org.brooklyn.ranger.client.ZookeeperClient;
import uk.org.brooklyn.ranger.context.ZkContext;
import uk.org.brooklyn.ranger.exception.ZkNodeNotExistException;

import java.util.stream.Collectors;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@ShellComponent
@ShellCommandGroup("BasicCommands")
public class BasicCommand {

    private final ZkContext zkContext;

    private final ZookeeperClient zkClient;

    @Autowired
    public BasicCommand(ZkContext zkContext, ZookeeperClient zkClient) {
        this.zkContext = zkContext;
        this.zkClient = zkClient;
    }

    @Bean
    public CommandRegistration pwd() {
        return CommandRegistration.builder()
                .command("pwd")
                .description("Prints current work node.")
                .withTarget().function(ctx -> zkContext.getCursor())
                .and()
                .build();
    }

    @Bean
    public CommandRegistration cd() {
        return CommandRegistration.builder()
                .command("cd")
                .description("Changes current work node.")
                .withOption()
                    .position(0)
                    .longNames("node")
                    .type(String.class)
                    .completion(ctx -> zkClient.children(zkContext.getCursor())
                        .stream().map(CompletionProposal::new)
                        .collect(Collectors.toList()))
                .and()
                .withTarget().function(this::changeCurrentWorkNode)
                .and()
                .build();
    }

    @Bean
    public CommandRegistration ls() {
        return CommandRegistration.builder()
                .command("ls")
                .description("Lists children of current work node.")
                .withOption()
                .longNames("node")
                .position(0)
                .required(false)
                .defaultValue(zkContext.getCursor())
                .and()
                .withTarget().function(this::listChildren)
                .and()
                .build();
    }

    private String changeCurrentWorkNode(CommandContext ctx) {
        String node = ctx.getOptionValue("node");
        try {
            node = findNode(node);
        } catch (ZkNodeNotExistException e) {
            ctx.getTerminal().writer().printf("Node [ %s ] does not exist...%n", e.getPath());
            return null;
        }
        zkContext.setCursor(node);
        return node;
    }

    private String listChildren(CommandContext ctx) {
        String node = ctx.getOptionValue("node");
        try {
            node = findNode(node);
        } catch (ZkNodeNotExistException e) {
            ctx.getTerminal().writer().printf("Node [ %s ] does not exist...%n", e.getPath());
            return null;
        }
        return String.join(" ", zkClient.children(findNode(node)));
    }

    private String findNode(String node) {
        if (node == null || node.isBlank()) {
            node = "/";
            return node;
        }

        final String cursor = zkContext.getCursor();

        if (node.endsWith("/")) {
            return findNode(node.substring(0, node.length() - 1));
        }

        if (".".equals(node)) {
            return cursor;
        }

        if ("..".equals(node)) {
            return findNode(cursor.substring(0, cursor.lastIndexOf('/')));
        }

        if (!node.startsWith("/")) {
            node = cursor +
                    (cursor.equals("/") ? "" : "/") +
                    node;
        }

        if (!zkClient.exists(node)) {
            throw new ZkNodeNotExistException(node);
        }

        return node;
    }
}
