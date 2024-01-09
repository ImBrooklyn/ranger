package uk.org.brooklyn.ranger.config;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;
import uk.org.brooklyn.ranger.context.ZkContext;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class ZkShellPromptProvider implements PromptProvider {

    private final ZkContext zkContext;

    @Autowired
    public ZkShellPromptProvider(ZkContext zkContext) {
        this.zkContext = zkContext;
    }

    @Override
    public AttributedString getPrompt() {
        return new AttributedString(
                "ranger: " + zkContext.getCursor() + " > ",
                AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW));
    }
}
