package uk.org.brooklyn.zkshell.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import uk.org.brooklyn.zkshell.context.ZkContext;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@ShellComponent
@ShellCommandGroup("BasicCommands")
public class BasicCommand {

    private final ZkContext zkContext;

    @Autowired
    public BasicCommand(ZkContext zkContext) {
        this.zkContext = zkContext;
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
                .withOption().position(0).longNames("node").type(String.class)
                .and()
                .withTarget().function(this::changeCurrentWorkNode)
                .and()
                .build();
    }

    private String changeCurrentWorkNode(CommandContext ctx) {
        String node = ctx.getOptionValue("node");
        return changeCurrentWorkNode(node);

    }

    private String changeCurrentWorkNode(String node) {
        if (node.startsWith("/")) {
            zkContext.setCursor(node);
            return zkContext.getCursor();
        }

        if (node.endsWith("/")) {
            return changeCurrentWorkNode(node.substring(0, node.length() - 1));
        }

        String sb = zkContext.getCursor() +
                (zkContext.getCursor().endsWith("/") ? "" : "/") +
                node;

        zkContext.setCursor(sb);
        return zkContext.getCursor();
    }
}
