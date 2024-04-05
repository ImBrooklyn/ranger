package uk.org.brooklyn.ranger.cmd;

import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;

/**
 * @author ImBrooklyn
 * @since 05/04/2024
 */
@ShellComponent
public class WriteCommand extends CommandBase {

    private static final String CMD_GROUP = "Basic write commands";

    @Bean
    public CommandRegistration touch() {
        return CommandRegistration.builder()
                .command("create")
                .withAlias().command("touch").and()
                .description("Create node.")
                .availability(cmdAvailability::connected)
                .group(CMD_GROUP)
                .withOption()
                    .position(0)
                    .longNames("name", "node")
                    .type(String.class)
                    .required(true)
                .and()
                .withOption()
                    .longNames("ephemeral")
                    .shortNames('e')
                    .type(boolean.class)
                    .defaultValue("false")
                .and()
                .withOption()
                    .longNames("sequential")
                    .shortNames('s')
                    .type(boolean.class)
                    .defaultValue("false")
                .and()
                .withTarget()
                .function(this::createNode)
                .and()
                .build();
    }

    private String createNode(CommandContext ctx) {
        String path = zkContext.newNode(ctx);
        if (path != null) {
            zkClient.create(path,
                    ctx.getOptionValue("ephemeral"),
                    ctx.getOptionValue("sequential"));
        }
        return path;
    }
}
