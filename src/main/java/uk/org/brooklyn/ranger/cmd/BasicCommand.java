package uk.org.brooklyn.ranger.cmd;

import org.springframework.context.annotation.Bean;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;

import java.util.stream.Collectors;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@ShellComponent
public class BasicCommand extends CommandBase {

    private static final String CMD_GROUP = "Basic commands";

    @Bean
    public CommandRegistration pwd() {
        return CommandRegistration.builder()
                .command("pwd")
                .description("Print current work node.")
                .availability(cmdAvailability::connected)
                .group(CMD_GROUP)
                .withTarget().function(ctx -> zkContext.getCursor())
                .and()
                .build();
    }

    @Bean
    public CommandRegistration cd() {
        return CommandRegistration.builder()
                .command("cd")
                .description("Change current work node.")
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
                .withTarget().function(zkContext::changeCurrentWorkNode)
                .and()
                .build();
    }

    @Bean
    public CommandRegistration ls() {
        return CommandRegistration.builder()
                .command("ls")
                .description("List children of current work node.")
                .availability(cmdAvailability::connected)
                .group(CMD_GROUP)
                .withOption()
                    .longNames("node")
                    .position(0)
                    .required(false)
                    .defaultValue(".")
                .and()
                .withTarget().function(zkContext::listChildren)
                .and()
                .build();
    }
}
