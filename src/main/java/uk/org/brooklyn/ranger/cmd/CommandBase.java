package uk.org.brooklyn.ranger.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import uk.org.brooklyn.ranger.client.ZookeeperClient;
import uk.org.brooklyn.ranger.context.CommandAvailability;
import uk.org.brooklyn.ranger.context.ZkContext;

/**
 * @author ImBrooklyn
 * @since 05/04/2024
 */
public abstract class CommandBase {

    protected ZkContext zkContext;

    protected CommandAvailability cmdAvailability;

    protected ZookeeperClient zkClient;

    @Autowired
    public void setZkContext(ZkContext zkContext) {
        this.zkContext = zkContext;
    }

    @Autowired
    public void setCmdAvailability(CommandAvailability cmdAvailability) {
        this.cmdAvailability = cmdAvailability;
    }

    @Autowired
    public void setZkClient(ZookeeperClient zkClient) {
        this.zkClient = zkClient;
    }
}
