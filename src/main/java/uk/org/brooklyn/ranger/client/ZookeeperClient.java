package uk.org.brooklyn.ranger.client;

import uk.org.brooklyn.ranger.model.NodeStat;

import java.util.List;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
public interface ZookeeperClient {

    boolean connect();

    boolean connected();

    boolean exists(String path);

    List<String> children(String path);

    NodeStat stat(String path);

}
