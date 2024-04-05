package uk.org.brooklyn.ranger.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.CommandContext;
import org.springframework.stereotype.Component;
import uk.org.brooklyn.ranger.client.ZookeeperClient;
import uk.org.brooklyn.ranger.exception.ZkNodeAlreadyExistException;
import uk.org.brooklyn.ranger.exception.ZkNodeNotExistException;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class ZkContext {

    private final ZookeeperClient zkClient;

    @Autowired
    public ZkContext(ZookeeperClient zkClient) {
        this.zkClient = zkClient;
        this.cursor = "/";
    }

    @Getter
    @Setter
    private String cursor;


    public String changeCurrentWorkNode(CommandContext ctx) {
        String node = getNode(ctx);
        if (node != null) {
            this.setCursor(node);
        }
        return node;
    }

    public String listChildren(CommandContext ctx) {
        String node = getNode(ctx);
        return node == null ? null : String.join(" ", zkClient.children(this.findNode(node)));
    }

    public String getNode(CommandContext ctx) {
        String node = ctx.getOptionValue("node");
        try {
            node = findNode(node);
        } catch (ZkNodeNotExistException e) {
            ctx.getTerminal().writer().printf("Node [ %s ] does not exist...%n", e.getPath());
            node = null;
        }

        return node;
    }

    public String newNode(CommandContext ctx) {
        String node = ctx.getOptionValue("node");
        try {
            node = findNode(node);
        } catch (ZkNodeNotExistException e) {
            return e.getPath();
        }
        ctx.getTerminal().writer().printf("Node [ %s ] already exist...%n", node);
        return null;
    }

    private String findNode(String node) {
        if (node == null || node.isBlank()) {
            node = "/";
            return node;
        }

        if (node.endsWith("/")) {
            return findNode(node.substring(0, node.length() - 1));
        }

        final String cursor = this.getCursor();
        switch (node) {
            case "." -> {
                return cursor;
            }
            case ".." -> {
                return findNode(cursor.substring(0, cursor.lastIndexOf('/')));
            }
            default -> {
                node = node.startsWith("/") ? node : (cursor + (cursor.equals("/") ? "" : "/") + node);
                if (!zkClient.exists(node)) {
                    throw new ZkNodeNotExistException(node);
                }
            }
        }
        return node;
    }
}
